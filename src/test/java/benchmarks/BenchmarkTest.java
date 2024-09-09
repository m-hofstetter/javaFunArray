package benchmarks;

import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import analysis.interval.IntervalAnalysisContext;
import benchmarks.sv.array_assert_loop_dep;
import benchmarks.sv.array_doub_access_init_const;
import benchmarks.sv.array_init_pair_sum_const;
import benchmarks.sv.array_monotonic;
import benchmarks.sv.array_ptr_single_elem_init_2;
import benchmarks.sv.array_shadowinit;
import benchmarks.sv.array_single_elem_init;
import benchmarks.sv.array_tiling_tcpy;
import benchmarks.sv.array_tripl_access_init_const;
import benchmarks.sv.brs1;
import benchmarks.sv.brs1f;
import benchmarks.sv.brs2;
import benchmarks.sv.brs2f;
import benchmarks.sv.brs3;
import benchmarks.sv.brs3f;
import benchmarks.sv.brs4;
import benchmarks.sv.brs4f;
import benchmarks.sv.brs5;
import benchmarks.sv.brs5f;
import benchmarks.sv.conda;
import benchmarks.sv.condaf;
import benchmarks.sv.condg;
import benchmarks.sv.condgf;
import benchmarks.sv.condm;
import benchmarks.sv.condmf;
import benchmarks.sv.condn;
import benchmarks.sv.condnf;
import benchmarks.sv.copysome1_1;
import benchmarks.sv.copysome1_2;
import benchmarks.sv.copysome2_1;
import benchmarks.sv.copysome2_2;
import benchmarks.sv.eqn1;
import benchmarks.sv.eqn1f;
import benchmarks.sv.eqn2;
import benchmarks.sv.eqn2f;
import benchmarks.sv.eqn3;
import benchmarks.sv.eqn3f;
import benchmarks.sv.eqn4;
import benchmarks.sv.eqn4f;
import benchmarks.sv.eqn5;
import benchmarks.sv.eqn5f;
import benchmarks.sv.ifcomp;
import benchmarks.sv.ifcompf;
import benchmarks.sv.ifeqn1;
import benchmarks.sv.ifeqn1f;
import benchmarks.sv.ifeqn2;
import benchmarks.sv.ifeqn2f;
import benchmarks.sv.ifeqn3;
import benchmarks.sv.ifeqn3f;
import benchmarks.sv.ifeqn4;
import benchmarks.sv.ifeqn4f;
import benchmarks.sv.ifeqn5;
import benchmarks.sv.ifeqn5f;
import benchmarks.sv.ifncomp;
import benchmarks.sv.ifncompf;
import benchmarks.sv.indp1;
import benchmarks.sv.indp1f;
import benchmarks.sv.indp2;
import benchmarks.sv.indp2f;
import benchmarks.sv.indp3;
import benchmarks.sv.indp3f;
import benchmarks.sv.indp4;
import benchmarks.sv.indp4f;
import benchmarks.sv.indp5;
import benchmarks.sv.indp5f;
import benchmarks.sv.modn;
import benchmarks.sv.modnf;
import benchmarks.sv.modp;
import benchmarks.sv.modpf;
import benchmarks.sv.mods;
import benchmarks.sv.modsf;
import benchmarks.sv.ms1;
import benchmarks.sv.ms1f;
import benchmarks.sv.ms2;
import benchmarks.sv.ms2f;
import benchmarks.sv.ms3;
import benchmarks.sv.ms3f;
import benchmarks.sv.ms4;
import benchmarks.sv.ms4f;
import benchmarks.sv.ms5;
import benchmarks.sv.ms5f;
import benchmarks.sv.ncomp;
import benchmarks.sv.ncompf;
import benchmarks.sv.nsqm;
import benchmarks.sv.nsqm_if;
import benchmarks.sv.nsqm_iff;
import benchmarks.sv.nsqmf;
import benchmarks.sv.partial_lesser_bound_1;
import benchmarks.sv.pcomp;
import benchmarks.sv.pcompf;
import benchmarks.sv.res1;
import benchmarks.sv.res1f;
import benchmarks.sv.res1o;
import benchmarks.sv.res1of;
import benchmarks.sv.res2;
import benchmarks.sv.res2f;
import benchmarks.sv.res2o;
import benchmarks.sv.res2of;
import benchmarks.sv.s12if;
import benchmarks.sv.s12iff;
import benchmarks.sv.s1if;
import benchmarks.sv.s1iff;
import benchmarks.sv.s1lif;
import benchmarks.sv.s1liff;
import benchmarks.sv.s22if;
import benchmarks.sv.s22iff;
import benchmarks.sv.s2if;
import benchmarks.sv.s2iff;
import benchmarks.sv.s2lif;
import benchmarks.sv.s2liff;
import benchmarks.sv.s32if;
import benchmarks.sv.s32iff;
import benchmarks.sv.s3if;
import benchmarks.sv.s3iff;
import benchmarks.sv.s3lif;
import benchmarks.sv.s3liff;
import benchmarks.sv.s42if;
import benchmarks.sv.s42iff;
import benchmarks.sv.s4if;
import benchmarks.sv.s4iff;
import benchmarks.sv.s4lif;
import benchmarks.sv.s4liff;
import benchmarks.sv.s52if;
import benchmarks.sv.s52iff;
import benchmarks.sv.s5if;
import benchmarks.sv.s5iff;
import benchmarks.sv.s5lif;
import benchmarks.sv.s5liff;
import benchmarks.sv.sanfoundry_27_ground;
import benchmarks.sv.sina1;
import benchmarks.sv.sina1f;
import benchmarks.sv.sina2;
import benchmarks.sv.sina2f;
import benchmarks.sv.sina3;
import benchmarks.sv.sina3f;
import benchmarks.sv.sina4;
import benchmarks.sv.sina4f;
import benchmarks.sv.sina5;
import benchmarks.sv.sina5f;
import benchmarks.sv.sorting_bubblesort_2_ground;
import benchmarks.sv.sorting_bubblesort_ground_1;
import benchmarks.sv.sorting_bubblesort_ground_2;
import benchmarks.sv.sorting_selectionsort_2_ground;
import benchmarks.sv.sorting_selectionsort_ground_1;
import benchmarks.sv.sorting_selectionsort_ground_2;
import benchmarks.sv.sqm;
import benchmarks.sv.sqm_if;
import benchmarks.sv.sqm_iff;
import benchmarks.sv.sqmf;
import benchmarks.sv.ss1f;
import benchmarks.sv.ss2f;
import benchmarks.sv.ss4f;
import benchmarks.sv.ssinaf;
import benchmarks.sv.standard_compareModified_ground;
import benchmarks.sv.standard_compare_ground;
import benchmarks.sv.standard_copy1_ground_1;
import benchmarks.sv.standard_copy1_ground_2;
import benchmarks.sv.standard_copy2_ground_1;
import benchmarks.sv.standard_copy2_ground_2;
import benchmarks.sv.standard_copy3_ground_1;
import benchmarks.sv.standard_copy3_ground_2;
import benchmarks.sv.standard_copy4_ground_1;
import benchmarks.sv.standard_copy4_ground_2;
import benchmarks.sv.standard_copy5_ground_1;
import benchmarks.sv.standard_copy5_ground_2;
import benchmarks.sv.standard_copy6_ground_1;
import benchmarks.sv.standard_copy6_ground_2;
import benchmarks.sv.standard_copy7_ground_1;
import benchmarks.sv.standard_copy7_ground_2;
import benchmarks.sv.standard_copy8_ground_1;
import benchmarks.sv.standard_copy8_ground_2;
import benchmarks.sv.standard_copy9_ground_1;
import benchmarks.sv.standard_copy9_ground_2;
import benchmarks.sv.standard_copyInitSum2_ground_1;
import benchmarks.sv.standard_copyInitSum2_ground_2;
import benchmarks.sv.standard_copyInitSum3_ground;
import benchmarks.sv.standard_copyInitSum_ground;
import benchmarks.sv.standard_copyInit_ground;
import benchmarks.sv.standard_init1_ground_1;
import benchmarks.sv.standard_init1_ground_2;
import benchmarks.sv.standard_init2_ground_1;
import benchmarks.sv.standard_init2_ground_2;
import benchmarks.sv.standard_init3_ground_1;
import benchmarks.sv.standard_init3_ground_2;
import benchmarks.sv.standard_init4_ground_1;
import benchmarks.sv.standard_init4_ground_2;
import benchmarks.sv.standard_init5_ground_1;
import benchmarks.sv.standard_init5_ground_2;
import benchmarks.sv.standard_init6_ground_1;
import benchmarks.sv.standard_init6_ground_2;
import benchmarks.sv.standard_init7_ground_1;
import benchmarks.sv.standard_init7_ground_2;
import benchmarks.sv.standard_init8_ground_1;
import benchmarks.sv.standard_init8_ground_2;
import benchmarks.sv.standard_init9_ground_1;
import benchmarks.sv.standard_init9_ground_2;
import benchmarks.sv.standard_maxInArray_ground;
import benchmarks.sv.standard_minInArray_ground_1;
import benchmarks.sv.standard_minInArray_ground_2;
import benchmarks.sv.standard_palindrome_ground;
import benchmarks.sv.standard_partial_init_ground;
import benchmarks.sv.standard_partition_ground_1;
import benchmarks.sv.standard_partition_ground_2;
import benchmarks.sv.standard_partition_original_ground;
import benchmarks.sv.standard_password_ground;
import benchmarks.sv.standard_reverse_ground;
import benchmarks.sv.standard_seq_init_ground;
import benchmarks.sv.standard_strcpy_ground_2;
import benchmarks.sv.standard_strcpy_original_1;
import benchmarks.sv.standard_two_index_01;
import benchmarks.sv.standard_two_index_02;
import benchmarks.sv.standard_two_index_03;
import benchmarks.sv.standard_two_index_04;
import benchmarks.sv.standard_two_index_05;
import benchmarks.sv.standard_two_index_06;
import benchmarks.sv.standard_two_index_07;
import benchmarks.sv.standard_two_index_08;
import benchmarks.sv.standard_two_index_09;
import benchmarks.sv.standard_vararg_ground;
import benchmarks.sv.standard_vector_difference_ground;
import benchmarks.sv.zero_sum1;
import benchmarks.sv.zero_sum2;
import benchmarks.sv.zero_sum3;
import benchmarks.sv.zero_sum4;
import benchmarks.sv.zero_sum5;
import benchmarks.sv.zero_sum_m2;
import benchmarks.sv.zero_sum_m3;
import benchmarks.sv.zero_sum_m4;
import benchmarks.sv.zero_sum_m5;
import funarray.state.ReachableState;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BenchmarkTest {

  private static final AnalysisContext<Interval, Interval> CONTEXT = IntervalAnalysisContext.INSTANCE;

  static Stream<Arguments> provideArguments() {
    return Stream.of(
            Arguments.of(new array_assert_loop_dep()),
            Arguments.of(new array_doub_access_init_const()),
            Arguments.of(new array_init_pair_sum_const()),
            Arguments.of(new array_monotonic()),
            Arguments.of(new array_shadowinit()),
            Arguments.of(new array_single_elem_init()),
            Arguments.of(new array_tiling_tcpy()),
            Arguments.of(new array_ptr_single_elem_init_2()),
            Arguments.of(new array_tripl_access_init_const()),
            Arguments.of(new brs1()),
            Arguments.of(new brs1f()),
            Arguments.of(new brs2()),
            Arguments.of(new brs2f()),
            Arguments.of(new brs3()),
            Arguments.of(new brs3f()),
            Arguments.of(new brs4()),
            Arguments.of(new brs4f()),
            Arguments.of(new brs5()),
            Arguments.of(new brs5f()),
            Arguments.of(new conda()),
            Arguments.of(new condaf()),
            Arguments.of(new condg()),
            Arguments.of(new condgf()),
            Arguments.of(new condm()),
            Arguments.of(new condmf()),
            Arguments.of(new condn()),
            Arguments.of(new condnf()),
            Arguments.of(new copysome1_1()),
            Arguments.of(new copysome1_2()),
            Arguments.of(new copysome2_1()),
            Arguments.of(new copysome2_2()),
            Arguments.of(new eqn1()),
            Arguments.of(new eqn1f()),
            Arguments.of(new eqn2()),
            Arguments.of(new eqn2f()),
            Arguments.of(new eqn3()),
            Arguments.of(new eqn3f()),
            Arguments.of(new eqn4()),
            Arguments.of(new eqn4f()),
            Arguments.of(new eqn5()),
            Arguments.of(new eqn5f()),
            Arguments.of(new ifcomp()),
            Arguments.of(new ifcompf()),
            Arguments.of(new ifeqn1()),
            Arguments.of(new ifeqn1f()),
            Arguments.of(new ifeqn2()),
            Arguments.of(new ifeqn2f()),
            Arguments.of(new ifeqn3()),
            Arguments.of(new ifeqn3f()),
            Arguments.of(new ifeqn4()),
            Arguments.of(new ifeqn4f()),
            Arguments.of(new ifeqn5()),
            Arguments.of(new ifeqn5f()),
            Arguments.of(new ifncomp()),
            Arguments.of(new ifncompf()),
            Arguments.of(new indp1()),
            Arguments.of(new indp1f()),
            Arguments.of(new indp2()),
            Arguments.of(new indp2f()),
            Arguments.of(new indp3()),
            Arguments.of(new indp3f()),
            Arguments.of(new indp4()),
            Arguments.of(new indp4f()),
            Arguments.of(new indp5()),
            Arguments.of(new indp5f()),
            Arguments.of(new modn()),
            Arguments.of(new modnf()),
            Arguments.of(new modp()),
            Arguments.of(new modpf()),
            Arguments.of(new mods()),
            Arguments.of(new modsf()),
            Arguments.of(new ms1()),
            Arguments.of(new ms1f()),
            Arguments.of(new ms2()),
            Arguments.of(new ms2f()),
            Arguments.of(new ms3()),
            Arguments.of(new ms3f()),
            Arguments.of(new ms4()),
            Arguments.of(new ms4f()),
            Arguments.of(new ms5()),
            Arguments.of(new ms5f()),
            Arguments.of(new ncomp()),
            Arguments.of(new ncompf()),
            Arguments.of(new nsqm()),
            Arguments.of(new nsqm_if()),
            Arguments.of(new nsqm_iff()),
            Arguments.of(new nsqmf()),
            Arguments.of(new partial_lesser_bound_1()),
            Arguments.of(new pcomp()),
            Arguments.of(new pcompf()),
            Arguments.of(new res1()),
            Arguments.of(new res1f()),
            Arguments.of(new res1o()),
            Arguments.of(new res1of()),
            Arguments.of(new res2()),
            Arguments.of(new res2f()),
            Arguments.of(new res2o()),
            Arguments.of(new res2of()),
            Arguments.of(new s1if()),
            Arguments.of(new s1iff()),
            Arguments.of(new s1lif()),
            Arguments.of(new s1liff()),
            Arguments.of(new s2if()),
            Arguments.of(new s2iff()),
            Arguments.of(new s2lif()),
            Arguments.of(new s2liff()),
            Arguments.of(new s3if()),
            Arguments.of(new s3iff()),
            Arguments.of(new s3lif()),
            Arguments.of(new s3liff()),
            Arguments.of(new s4if()),
            Arguments.of(new s4iff()),
            Arguments.of(new s4lif()),
            Arguments.of(new s4liff()),
            Arguments.of(new s5if()),
            Arguments.of(new s5iff()),
            Arguments.of(new s5lif()),
            Arguments.of(new s5liff()),
            Arguments.of(new s12if()),
            Arguments.of(new s12iff()),
            Arguments.of(new s22if()),
            Arguments.of(new s22iff()),
            Arguments.of(new s32if()),
            Arguments.of(new s32iff()),
            Arguments.of(new s42if()),
            Arguments.of(new s42iff()),
            Arguments.of(new s52if()),
            Arguments.of(new s52iff()),
            Arguments.of(new sanfoundry_27_ground()),
            Arguments.of(new sina1()),
            Arguments.of(new sina1f()),
            Arguments.of(new sina2()),
            Arguments.of(new sina2f()),
            Arguments.of(new sina3()),
            Arguments.of(new sina3f()),
            Arguments.of(new sina4()),
            Arguments.of(new sina4f()),
            Arguments.of(new sina5()),
            Arguments.of(new sina5f()),
            Arguments.of(new sorting_bubblesort_2_ground()),
            Arguments.of(new sorting_bubblesort_ground_1()),
            Arguments.of(new sorting_bubblesort_ground_2()),
            Arguments.of(new sorting_selectionsort_2_ground()),
            Arguments.of(new sorting_selectionsort_ground_1()),
            Arguments.of(new sorting_selectionsort_ground_2()),
            Arguments.of(new sqm()),
            Arguments.of(new sqm_if()),
            Arguments.of(new sqm_iff()),
            Arguments.of(new sqmf()),
            Arguments.of(new ss1f()),
            Arguments.of(new ss2f()),
            Arguments.of(new ss4f()),
            Arguments.of(new ssinaf()),
            Arguments.of(new standard_compare_ground()),
            Arguments.of(new standard_compareModified_ground()),
            Arguments.of(new standard_copy1_ground_1()),
            Arguments.of(new standard_copy1_ground_2()),
            Arguments.of(new standard_copy2_ground_1()),
            Arguments.of(new standard_copy2_ground_2()),
            Arguments.of(new standard_copy3_ground_1()),
            Arguments.of(new standard_copy3_ground_2()),
            Arguments.of(new standard_copy4_ground_1()),
            Arguments.of(new standard_copy4_ground_2()),
            Arguments.of(new standard_copy5_ground_1()),
            Arguments.of(new standard_copy5_ground_2()),
            Arguments.of(new standard_copy6_ground_1()),
            Arguments.of(new standard_copy6_ground_2()),
            Arguments.of(new standard_copy7_ground_1()),
            Arguments.of(new standard_copy7_ground_2()),
            Arguments.of(new standard_copy8_ground_1()),
            Arguments.of(new standard_copy8_ground_2()),
            Arguments.of(new standard_copy9_ground_1()),
            Arguments.of(new standard_copy9_ground_2()),
            Arguments.of(new standard_copyInit_ground()),
            Arguments.of(new standard_copyInitSum2_ground_1()),
            Arguments.of(new standard_copyInitSum2_ground_2()),
            Arguments.of(new standard_copyInitSum3_ground()),
            Arguments.of(new standard_copyInitSum_ground()),
            Arguments.of(new standard_init1_ground_1()),
            Arguments.of(new standard_init1_ground_2()),
            Arguments.of(new standard_init2_ground_1()),
            Arguments.of(new standard_init2_ground_2()),
            Arguments.of(new standard_init3_ground_1()),
            Arguments.of(new standard_init3_ground_2()),
            Arguments.of(new standard_init4_ground_1()),
            Arguments.of(new standard_init4_ground_2()),
            Arguments.of(new standard_init5_ground_1()),
            Arguments.of(new standard_init5_ground_2()),
            Arguments.of(new standard_init6_ground_1()),
            Arguments.of(new standard_init6_ground_2()),
            Arguments.of(new standard_init7_ground_1()),
            Arguments.of(new standard_init7_ground_2()),
            Arguments.of(new standard_init8_ground_1()),
            Arguments.of(new standard_init8_ground_2()),
            Arguments.of(new standard_init9_ground_1()),
            Arguments.of(new standard_init9_ground_2()),
            Arguments.of(new standard_maxInArray_ground()),
            Arguments.of(new standard_minInArray_ground_1()),
            Arguments.of(new standard_minInArray_ground_2()),
            Arguments.of(new standard_palindrome_ground()),
            Arguments.of(new standard_partial_init_ground()),
            Arguments.of(new standard_partition_ground_1()),
            Arguments.of(new standard_partition_ground_2()),
            Arguments.of(new standard_partition_original_ground()),
            Arguments.of(new standard_password_ground()),
            Arguments.of(new standard_reverse_ground()),
            Arguments.of(new standard_seq_init_ground()),
            Arguments.of(new standard_strcpy_ground_2()),
            Arguments.of(new standard_strcpy_original_1()),
            Arguments.of(new standard_two_index_01()),
            Arguments.of(new standard_two_index_02()),
            Arguments.of(new standard_two_index_03()),
            Arguments.of(new standard_two_index_04()),
            Arguments.of(new standard_two_index_05()),
            Arguments.of(new standard_two_index_06()),
            Arguments.of(new standard_two_index_07()),
            Arguments.of(new standard_two_index_08()),
            Arguments.of(new standard_two_index_09()),
            Arguments.of(new standard_vararg_ground()),
            Arguments.of(new standard_vector_difference_ground()),
            Arguments.of(new zero_sum1()),
            Arguments.of(new zero_sum2()),
            Arguments.of(new zero_sum3()),
            Arguments.of(new zero_sum4()),
            Arguments.of(new zero_sum5()),
            Arguments.of(new zero_sum_m2()),
            Arguments.of(new zero_sum_m3()),
            Arguments.of(new zero_sum_m4()),
            Arguments.of(new zero_sum_m5())
    );
  }

  static int positiveAssertions = 0;
  static int negativeAssertions = 0;
  static int assertionCount = 0;
  static int errorCount = 0;
  static int testCount = 0;

  @Timeout(1)
  @ParameterizedTest
  @MethodSource("provideArguments")
  void test(Benchmark benchmark) {
    var builder = new IntervalAnalysisBenchmarkBuilder();

    var analysis = benchmark.statement(builder);

    var startingState = new ReachableState<>(
            benchmark.integerVariables(),
            benchmark.arrayVariables(),
            CONTEXT
    );

    testCount++;

    AnalysisResult<Interval, Interval> result;

    try {
      result = analysis.run(startingState);
    } catch (Exception e) {
      errorCount++;
      throw e;
    }


    positiveAssertions += result.assertions().positive();
    negativeAssertions += result.assertions().negative();
    assertionCount += result.assertions().positive() + result.assertions().negative() + result.assertions().indeterminable();


    System.out.println("Test: " + benchmark.getClass().getSimpleName());
    System.out.println(result.protocol());
    System.out.println("\n\n\n");

    if (result.assertions().negative() != 0) {
      throw new RuntimeException("Negative assertion in: " + benchmark.getClass().getSimpleName());
    }
  }

  @AfterAll
  static void afterAll() {
    System.out.printf("""
                                
                                
                    ======= RESULTS =======
                                
                    Positive assertions: %d out of %d (%2.2f%%)
                    Negative assertions: %d out of %d (%2.2f%%)
                    Errors: %d out of %d tests run (%2.2f%%)
                    %n""", positiveAssertions, assertionCount, (positiveAssertions * 100f) / assertionCount,
            negativeAssertions, assertionCount, (negativeAssertions * 100f) / assertionCount,
            errorCount, testCount, (errorCount * 100f) / testCount);
  }
}
