# Daikon Invariant Extraction Results

## Summary
- **Total invariant files generated**: 153 (txt + inv.gz)
- **Projects processed**: 28
- **Successful projects**: 26
- **Failed projects**: 2 (Android dependencies missing)

## Execution Details
- **Tool**: Daikon 5.8.22
- **Options**: `--suppress_redundant` (removes redundant invariants)
- **Pipeline**: DynComp → Chicory → Daikon → PrintInvariants

## Results by Project

| Project | Cases | Files | Status |
|---------|-------|-------|--------|
| adempiere | 2 | 6 | ✓ |
| alibaba_druid | 2 | 6 | ✓ |
| android_rcs_rcsjta | 1 | 3 | ✓ |
| androiduil | 1 | 3 | ✓ |
| apache_gora | 2 | 6 | ✓ |
| asterisk_java | 2 | 6 | ✓ |
| calligraphy | 2 | 6 | ✓ |
| cego | 1 | 3 | ✓ |
| gnucrasha | 2 | 6 | ✓ |
| hoverruan_weiboclient4j | 1 | 3 | ✓ |
| ivantrendafilov_confucius | 9 | 27 | ✓ |
| jmrtd | 2 | 6 | ✓ |
| jriecken_gae_java_mini_profiler | 1 | 3 | ✓ |
| lnreadera | 2 | 6 | ✓ |
| logblock_logblock_2 | 1 | 3 | ✓ |
| mqtt | 1 | 3 | ✓ |
| onosendai | 1 | 0 | ✗ (Android SDK missing) |
| openaiab | 1 | 0 | ✗ (Android SDK missing) |
| pawotag | 1 | 3 | ✓ |
| rhino | 1 | 3 | ✓ |
| screen_notifications | 1 | 3 | ✓ |
| tap_apps | 1 | 3 | ✓ |
| tbuktu_ntru | 4 | 12 | ✓ |
| testng | 5 | 15 | ✓ |
| thomas_s_b_visualee | 3 | 9 | ✓ |
| tucanmobile | 1 | 3 | ✓ |
| ushahidia | 1 | 3 | ✓ |
| wordpressa | 2 | 3 | ✓ (partial) |

## Directory Structure
\`\`\`
invariant/
├── {project_name}/
│   ├── {case_num}/
│   │   ├── original/
│   │   │   ├── invariants.inv.gz  # Binary invariant file
│   │   │   └── invariants.txt     # Human-readable text
│   │   ├── misuse/
│   │   │   ├── invariants.inv.gz
│   │   │   └── invariants.txt
│   │   └── fixed/
│   │       ├── invariants.inv.gz
│   │       └── invariants.txt
\`\`\`

## Notes
- Each variant (original, misuse, fixed) has its own invariant extraction
- CommonCases directories are excluded as they contain common test code
- Some projects have Driver class compatibility issues but still generate invariants
