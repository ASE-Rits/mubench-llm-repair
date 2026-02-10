# Diff Files - Code Variants Comparison

## Overview
This directory contains unified diff files comparing three code variants for each test case:
- **original**: The correct implementation
- **misuse**: Code with API misuse bugs  
- **fixed**: The corrected version of the misuse

## Statistics
- **Total diff files**: 162
- **Test cases**: 54
- **Projects**: 28
- **Comparisons per case**: 3 (original↔misuse, misuse↔fixed, original↔fixed)

## File Naming Convention
```
{Project}Test_{case}.{Project}Test_{case}_{comparison}.diff
```

Where:
- `{Project}`: CamelCase project name (e.g., AndroidRcsRcsjta, TestNG)
- `{case}`: Case number (e.g., _1, _21, _100)
- `{comparison}`: One of:
  - `original_vs_misuse`: Shows the bug introduction
  - `misuse_vs_fixed`: Shows the bug fix
  - `original_vs_fixed`: Shows overall changes (if any)

## Examples
```
AndroidRcsRcsjtaTest_1.AndroidRcsRcsjtaTest_1_misuse_vs_fixed.diff
TestngTest_21.TestngTest_21_original_vs_misuse.diff
IvantrendafilovConfuciusTest_100.IvantrendafilovConfuciusTest_100_original_vs_fixed.diff
```

## Generation
Generated using standard Linux `diff -r -u` command:
```bash
diff -r -u {variant1_dir} {variant2_dir} > output.diff
```

See: [generate_diffs.sh](../generate_diffs.sh) for the complete generation script.

## Projects Included (54 test cases)
- AdempiereTest_1
- AdempiereTest_2
- AlibabaDruidTest_1
- AlibabaDruidTest_2
- AndroidRcsRcsjtaTest_1
- AndroiduilTest_1
- ApacheGoraTest_56_1
- ApacheGoraTest_56_2
- AsteriskJavaTest_194
- AsteriskJavaTest_81
- CalligraphyTest_1
- CalligraphyTest_2
- CegoTest_1
- GnucrashaTest_1a
- GnucrashaTest_1b
- HoverruanWeiboclient4jTest_128
- IvantrendafilovConfuciusTest_100
- IvantrendafilovConfuciusTest_101
- IvantrendafilovConfuciusTest_93
- IvantrendafilovConfuciusTest_94
- IvantrendafilovConfuciusTest_95
- IvantrendafilovConfuciusTest_96
- IvantrendafilovConfuciusTest_97
- IvantrendafilovConfuciusTest_98
- IvantrendafilovConfuciusTest_99
- JmrtdTest_1
- JmrtdTest_2
- JrieckenGaeJavaMiniProfilerTest_39
- LnreaderaTest_1
- LnreaderaTest_2
- LogblockLogblock2Test_15
- MqttTest_389
- OnosendaiTest_1
- OpenaiabTest_1
- PawotagTest_1
- RhinoTest_1
- ScreenNotificationsTest_1
- TapAppsTest_1
- TbuktuNtruTest_473
- TbuktuNtruTest_474
- TbuktuNtruTest_475
- TbuktuNtruTest_476
- TestngTest_16
- TestngTest_17
- TestngTest_18
- TestngTest_21
- TestngTest_22
- ThomasSBVisualeeTest_29
- ThomasSBVisualeeTest_30
- ThomasSBVisualeeTest_32
- TucanmobileTest_1
- UshahidiaTest_1
- WordpressaTest_1
- WordpressaTest_3

## Usage
These diffs are useful for:
- Analyzing API misuse patterns
- Understanding bug fixes
- Training machine learning models on code repair
- Manual code review and learning
