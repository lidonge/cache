package cache.simpledemo;


import cache.ICompositeKey;
import cache.ILogable;
import cache.simpledemo.impls.client.Client;
import cache.simpledemo.impls.client.ClientCache;
import cache.simpledemo.impls.client.PhysicalClientCache;
import cache.simpledemo.impls.client.VirtualCenterInClient;
import cache.simpledemo.impls.source.Center;
import cache.simpledemo.impls.source.Source;
import cache.simpledemo.impls.source.VirtualCenterInSource;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class AllInOneDemo implements ILogable {
    private Source source;
    private Center center;
    //    private Client[] clients;
    private int clientCount = 2;
    private ICompositeKey keys_1 = new ICompositeKey() {
        String[] keys = new String[]{"categorize", "type", "name"};
        @Override
        public String getCompositeKey() {
            return ICompositeKey.compositeKeys(keys,getSeparator());
        }

        @Override
        public String getSeparator() {
            return "_";
        }
    };

    private Worker<Client>[] clientWorkers;
    private Worker<Source> srcWorker;
    public static void main(String[] args) {
        AllInOneDemo demo = new AllInOneDemo();
//        demo.scenarioClientGet(demo.clients[0], demo.keys_1);
        demo.updateAndGet();
    }

    public AllInOneDemo() {
        initSource();
        initCenter();
        initClients();
    }

    private void initCenter() {
        center = new Center();
        source.setVirtualCenter(new VirtualCenterInSource(center));
    }

    void updateAndGet(){
        allClientGet();
        waitForWorkersFinish(clientWorkers);
        getLogger().info("==========================");
        scenarioDataUpdated();
        waitForWorkersFinish(new Worker[]{srcWorker});
        allClientGet();
    }

    private void waitForWorkersFinish(Worker[] workers) {
        boolean allFin = true;
        synchronized (this){
            do{
                allFin = true;
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(Worker worker: workers) {
                    allFin &= worker.isFinished();
                }
            }while(!allFin);
        }
    }

    private void allClientGet() {
        for(Worker<Client> clinet: clientWorkers) {
            scenarioClientGet(clinet,keys_1);
        }
    }

    void scenarioClientGet(Worker<Client> worker, ICompositeKey keys){
        worker.setTask(() ->{
            Client client =worker.getTarget();
            getLogger().info("{} scenarioClientGet value is {}.",client.getName(),client.getCache().get(keys));
        });

    }
    void scenarioDataUpdated(){
        //source data updated
        srcWorker.setTask(() ->{
            source.onContentChanged(keys_1);
        });

    }

    private void initSource() {
        source = new Source();
        srcWorker = new Worker(source);
        new Thread(srcWorker).start();
    }

    void initClients(){
//        clients = new Client[clientCount];
        clientWorkers = new Worker[clientCount];
        for(int i =0;i<clientCount;i++){
//            clients[i] = initClient();
            clientWorkers[i] = new Worker(initClient());
            new Thread(clientWorkers[i]).start();
        }
    }

    Client initClient(){
        PhysicalClientCache pcc = new PhysicalClientCache();
        ClientCache cache = new ClientCache(pcc);

        Client ret  = new Client(true,cache );
        VirtualCenterInClient virtualCenterInClient = new VirtualCenterInClient(ret);
        pcc.setVirtualCenter(virtualCenterInClient);
        virtualCenterInClient.setVirtualCenterInSource(source.getCenter());
        virtualCenterInClient.registerClient(ret.getName(), ret);

        cache.setClient(ret);
        pcc.setClient(ret);
        return ret;
    }
}
class Worker<T> implements Runnable{
    ITask task;
    T target;

    public Worker(T target) {
        this.target = target;
    }

    T getTarget(){
        return target;
    }
    @Override
    public void run() {
        while(true){
            synchronized (this){
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(task != null) {
                task.exec();
                task = null;
            }
        }
    }
    void setTask(ITask task){
        this.task = task;
        synchronized (this){
            this.notify();
        }
    }

    boolean isFinished(){
        synchronized (this){
            return task == null;
        }
    }
}

@FunctionalInterface
interface ITask{
    void exec();
}