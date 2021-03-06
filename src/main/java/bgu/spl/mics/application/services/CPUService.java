package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.DataBatch;

/**
 * CPU service is responsible for handling the {@link  //DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    //---------------------Fields----------------------
    private boolean terminated;
    private final CPU cpu; //
    private final Cluster cluster;
    int coreNum;


    //-----------------Constructor---------------------
    public CPUService(String name, CPU _cpu) {
        super(name);
        cpu = _cpu;
        cluster = Cluster.getInstance();
        coreNum = cpu.getNumOfCores();
    }

    //-------------------Methods----------------------

    @Override
    protected void initialize() {
        Thread CPUProcessingThread = new Thread(cpu::processData);
        DataBatch db = cluster.getNextDataToBePreprocessed();
        if (db == null) {
            CPUProcessingThread.interrupt();
            cpu.terminate();
            terminate();
        }
        else
            cpu.startUp(db);


        CPUProcessingThread.start();

        subscribeBroadcast(TickBroadcast.class, c -> {
            cpu.getMoreTime();
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
            cpu.terminate();
            CPUProcessingThread.interrupt();
            terminate();
        });
    }
}
