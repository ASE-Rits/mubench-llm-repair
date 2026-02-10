package daikon_runner;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Daikon用の汎用ランナー
 * 各プロジェクトのDriverクラスを動的に呼び出し、メソッドを実行する
 * 複数のコンストラクタパターンをサポート
 */
public class DaikonRunner {
    
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: DaikonRunner <project.case_num> <variant>");
            System.err.println("Example: DaikonRunner testng._21 original");
            System.exit(1);
        }
        
        String projectCase = args[0];  // e.g., "testng._21"
        String variant = args[1];       // e.g., "original", "misuse", "fixed"
        
        String driverClassName = projectCase + ".Driver";
        
        try {
            Class<?> driverClass = Class.forName(driverClassName);
            Object driver = createDriverInstance(driverClass, variant);
            
            if (driver == null) {
                System.err.println("Could not create Driver instance for: " + driverClassName);
                System.exit(1);
            }
            
            // Driverの全publicメソッドを実行（引数なしのもの）
            for (Method method : driverClass.getMethods()) {
                // Object由来のメソッドとstaticメソッドはスキップ
                if (method.getDeclaringClass() == Object.class) continue;
                if (java.lang.reflect.Modifier.isStatic(method.getModifiers())) continue;
                
                // 引数なしのメソッドを試行
                if (method.getParameterCount() == 0) {
                    try {
                        System.out.println("Invoking: " + method.getName());
                        Object result = method.invoke(driver);
                        System.out.println("  Result: " + result);
                    } catch (Exception e) {
                        System.out.println("  Exception: " + e.getMessage());
                    }
                }
            }
            
            System.out.println("DaikonRunner completed for: " + projectCase + "/" + variant);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Driver class not found: " + driverClassName);
            System.exit(1);
        }
    }
    
    /**
     * 複数のコンストラクタパターンを試してDriverインスタンスを作成
     */
    private static Object createDriverInstance(Class<?> driverClass, String variant) {
        // パターン1: Driver(String variant)
        try {
            Constructor<?> ctor = driverClass.getConstructor(String.class);
            return ctor.newInstance(variant);
        } catch (Exception e) { /* try next */ }
        
        // パターン2: Driver(String variant, Properties)
        try {
            Constructor<?> ctor = driverClass.getConstructor(String.class, Properties.class);
            Properties props = new Properties();
            props.setProperty("test.key", "test.value");
            props.setProperty("byte.key", "1");
            props.setProperty("short.key", "2");
            props.setProperty("int.key", "3");
            props.setProperty("long.key", "4");
            props.setProperty("float.key", "1.5");
            props.setProperty("double.key", "2.5");
            props.setProperty("boolean.key", "true");
            return ctor.newInstance(variant, props);
        } catch (Exception e) { /* try next */ }
        
        // パターン3: Driver(String variant, String[], int[]) - MQTTなど
        try {
            Constructor<?> ctor = driverClass.getConstructor(String.class, String[].class, int[].class);
            return ctor.newInstance(variant, new String[]{"topic1"}, new int[]{0});
        } catch (Exception e) { /* try next */ }
        
        // パターン4: Driver(String variant, byte, byte[]) - MQTTなど
        try {
            Constructor<?> ctor = driverClass.getConstructor(String.class, byte.class, byte[].class);
            return ctor.newInstance(variant, (byte)0, new byte[]{0, 0, 1});
        } catch (Exception e) { /* try next */ }
        
        // パターン5: Driver() - 引数なし
        try {
            Constructor<?> ctor = driverClass.getConstructor();
            return ctor.newInstance();
        } catch (Exception e) { /* try next */ }
        
        // パターン6: 最初のコンストラクタを試す（全引数にデフォルト値）
        try {
            Constructor<?>[] ctors = driverClass.getConstructors();
            if (ctors.length > 0) {
                Constructor<?> ctor = ctors[0];
                Class<?>[] paramTypes = ctor.getParameterTypes();
                Object[] args = new Object[paramTypes.length];
                
                for (int i = 0; i < paramTypes.length; i++) {
                    args[i] = getDefaultValue(paramTypes[i], variant);
                }
                return ctor.newInstance(args);
            }
        } catch (Exception e) { /* give up */ }
        
        return null;
    }
    
    /**
     * 型に応じたデフォルト値を返す
     */
    private static Object getDefaultValue(Class<?> type, String variant) {
        if (type == String.class) return variant;
        if (type == int.class || type == Integer.class) return 0;
        if (type == long.class || type == Long.class) return 0L;
        if (type == double.class || type == Double.class) return 0.0;
        if (type == float.class || type == Float.class) return 0.0f;
        if (type == boolean.class || type == Boolean.class) return false;
        if (type == byte.class || type == Byte.class) return (byte)0;
        if (type == short.class || type == Short.class) return (short)0;
        if (type == char.class || type == Character.class) return ' ';
        if (type == byte[].class) return new byte[]{0};
        if (type == int[].class) return new int[]{0};
        if (type == String[].class) return new String[]{"test"};
        if (type == Properties.class) {
            Properties p = new Properties();
            p.setProperty("test", "value");
            return p;
        }
        if (type == List.class) return new ArrayList<>();
        return null;
    }
}
