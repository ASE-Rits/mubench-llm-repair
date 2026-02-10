package rhino;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import rhino._1.Driver;


import static org.junit.Assert.*;

/**
 * 動的テスト: Parserのfunction()メソッドが正しくinitFunctionを呼び出すか検証。
 * 
 * バグ: IRFactory.initFunction()が同一実行パスで2回呼び出される
 * - Original: 1回のみ呼び出し → パース成功
 * - Misuse: 2回呼び出し → 無限ループ（StackOverflowError）
 * 
 * 動的テストではネストした関数定義をパースし、タイムアウトで無限ループを検出する。
 */
class RhinoTest_1 {

    // 無限ループ検出用のタイムアウト（秒）
    private static final int PARSE_TIMEOUT_SECONDS = 5;

    abstract static class CommonCases {
        abstract Driver driver() throws Exception;

        /**
         * 単純な関数定義のパーステスト
         */
        @Test(timeout = 5000)
        public void testParseSimpleFunction() throws Exception {
            Driver d = driver();
            String code = "function foo() { return 1; }";
            
            Object result = d.parse(code, "test.js", 1);
            assertNotNull("Parser should return non-null result", result);
        }

        /**
         * ネストした関数定義のパーステスト
         * バグがある場合、この関数定義で無限ループが発生する
         */
        @Test(timeout = 5000)
        public void testParseNestedFunction() throws Exception {
            Driver d = driver();
            // ネストした関数定義 - バグがあると無限ループ
            String code = "function outer() { function inner() { return 1; } return inner(); }";
            
            Object result = d.parse(code, "test.js", 1);
            assertNotNull("Parser should return non-null result for nested functions", result);
        }

        /**
         * 複数のネストレベルを持つ関数定義のパーステスト
         */
        @Test(timeout = 5000)
        public void testParseDeeplyNestedFunctions() throws Exception {
            Driver d = driver();
            String code = 
                "function a() { " +
                "  function b() { " +
                "    function c() { return 1; } " +
                "    return c(); " +
                "  } " +
                "  return b(); " +
                "}";
            
            Object result = d.parse(code, "test.js", 1);
            assertNotNull("Parser should handle deeply nested functions", result);
        }

        /**
         * 関数式（function expression）のパーステスト
         */
        @Test(timeout = 5000)
        public void testParseFunctionExpression() throws Exception {
            Driver d = driver();
            String code = "var f = function() { var g = function() { return 1; }; return g(); };";
            
            Object result = d.parse(code, "test.js", 1);
            assertNotNull("Parser should handle function expressions", result);
        }

        /**
         * 静的解析: initFunctionが1回だけ呼ばれるパターンか確認（後方互換性のため）
         */
        @Test
        public void testInitFunctionCalledOnce() throws Exception {
            Driver d = driver();
            assertTrue("initFunction should be called exactly once per function", d.hasCorrectInitFunctionPattern());
        }
    }

    // --- 実行定義 ---
    public static class Original extends CommonCases {
        @Override
        Driver driver() throws Exception {
            return new Driver("original");
        }
    }

    // Misuse: initFunctionが2回呼ばれる → 無限ループでタイムアウト
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() throws Exception {
            return new Driver("misuse");
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() throws Exception {
            return new Driver("fixed");
        }
    }
}
