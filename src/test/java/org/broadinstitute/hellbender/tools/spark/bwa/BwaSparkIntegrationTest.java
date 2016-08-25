package org.broadinstitute.hellbender.tools.spark.bwa;

import org.broadinstitute.hellbender.CommandLineProgramTest;
import org.broadinstitute.hellbender.utils.test.ArgumentsBuilder;
import org.broadinstitute.hellbender.utils.test.SamAssertionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public final class BwaSparkIntegrationTest extends CommandLineProgramTest {

    @Override
    public String getTestedClassName() {
        return BwaSpark.class.getSimpleName();
    }

    @Test
    public void test() throws Exception {
        final File expectedSam = getTestFile("bwa.sam");

        final File ref = getTestFile("ref.fa");
        final File input = getTestFile("R.bam");
        final File output = createTempFile("bwa", ".bam");
        if (!output.delete()) {
            Assert.fail();
        }

        ArgumentsBuilder args = new ArgumentsBuilder();
        args.addFileArgument("reference", ref);
        args.addFileArgument("input", input);
        args.add("disableSequenceDictionaryValidation=true"); // disable since input does not have a sequence dictionary
        args.addArgument("shardedOutput", "true");
        args.add("numReducers=1");
        args.addOutput(output);
        this.runCommandLine(args.getArgsArray());

        SamAssertionUtils.assertSamsEqual(new File(output, "part-r-00000.bam"), expectedSam);
    }

    @Test
    public void testUnkownSamFormat(){
        final File input = new File("test.sam");
        final File output = new File("out.bam");
        ArgumentsBuilder args = new ArgumentsBuilder();
        args.addInput(input);
        args.addOutput(output);
        args.addReference(new File(b37_reference_20_21));
        args.addBooleanArgument("disableSequenceDictionaryValidation", true);
        this.runCommandLine(args.getArgsArray());
    }

}
