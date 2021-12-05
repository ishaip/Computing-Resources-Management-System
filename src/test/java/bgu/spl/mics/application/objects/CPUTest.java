package bgu.spl.mics.application.objects;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CPUTest {

    private CPU cpu;

    @BeforeEach
    public void setUp() throws Exception {
        cpu = new CPU(4);

    }
//commit
    @Test
    public void testGetNumOfCPUs() {
        assertEquals(4, cpu.getNumOfCPUs());
    }

    @Test
    public void testProcess() throws Exception{
        assertFalse(cpu.isDone());
        cpu.process();
        assertTrue(cpu.isDone());
    }

    @Test
    public void testIsDone() {
        assertFalse(cpu.isDone());

        Data d = new Data(Data.Type.Text, 2000);
        DataBatch db = new DataBatch(d, 0);
        cpu.addBatchOfData(db);
        ArrayList<DataBatch> someData = new ArrayList<>();
        someData.add(db);

        cpu.process(someData);
        assertTrue(cpu.isDone());
    }
}