package cache.simpledemo;


import cache.ICacheData;
import cache.ICompositeKey;
import cache.client.IPhysicalCache;
import cache.impls.client.CaffeineClientCache;
import cache.simpledemo.impls.CompKey;
import cache.simpledemo.impls.client.*;
import cache.util.ILogable;
import cache.client.IBusinessService;
import cache.IClientCacheData;
import cache.client.ServiceCallException;
import cache.simpledemo.impls.DemoCacheData;
import cache.simpledemo.impls.source.Center;
import cache.simpledemo.impls.source.Source;
import cache.simpledemo.impls.source.VirtualCenterInSource;

import java.io.IOException;

/**
 * @author lidong@date 2023-10-25@version 1.0
 */
public class AllInOneDemo implements ILogable {
    private Source source;
    private Center center;
    //    private Client[] clients;
    private int clientCount = 2;
    private ICompositeKey keys[] = new ICompositeKey[]{
            new CompKey("user:0"),
            new CompKey("user:1"),
            new CompKey("user:2")
    };
    private ICompositeKey keys_1 = new ICompositeKey() {
        String[] keys = new String[]{"categorize", "type", "name"};

        @Override
        public String getCompositeKey() {
            return ICompositeKey.compositeKeys(keys, getSeparator());
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
//        demo.scenarioClientGet(demo.clientWorkers[0], demo.keys_1);
//        demo.updateAndGet();
        try {
            new CLI(demo);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    void updateAndGet() {
        allClientGet();
        waitForWorkersFinish(clientWorkers);
        getLogger().info("==========================");
        scenarioDataUpdated();
        waitForWorkersFinish(new Worker[]{srcWorker});
        allClientGet();
    }

    private void waitForWorkersFinish(Worker[] workers) {
        boolean allFin = true;
        synchronized (this) {
            do {
                allFin = true;
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (Worker worker : workers) {
                    allFin &= worker.isFinished();
                }
            } while (!allFin);
        }
    }

    private void allClientGet() {
        for (Worker<Client> clinet : clientWorkers) {
            scenarioClientGet(clinet, keys_1);
        }
    }

    void scenarioClientGet(int client, int key) {
        scenarioClientGet(clientWorkers[client], keys[key]);
    }

    void scenarioClientGet(Worker<Client> worker, ICompositeKey keys) {
        worker.setTask(() -> {
            Client client = worker.getTarget();
            IClientCacheData cacheData = client.getCache().getAndPutIfDirty(keys, new IBusinessService() {
                @Override
                public IClientCacheData getData() {
                    return new DemoCacheData(keys.getCompositeKey());
                }
            });
            getLogger().info("{} scenarioClientGet value is {}, new data put to cache.", client.getName(), cacheData);
        });

    }

    void scenarioDataUpdated(int key) {
        source.onContentChanged(keys[key]);
    }

    void scenarioDataUpdated() {
        //source data updated
        srcWorker.setTask(() -> {
            source.onContentChanged(keys_1);
        });

    }

    private void initSource() {
        source = new Source();
        srcWorker = new Worker(source);
        new Thread(srcWorker).start();
    }

    void initClients() {
//        clients = new Client[clientCount];
        clientWorkers = new Worker[clientCount];
        for (int i = 0; i < clientCount; i++) {
//            clients[i] = initClient();
            clientWorkers[i] = new Worker(initClient());
            new Thread(clientWorkers[i]).start();
        }
    }

    Client initClient() {
//        IPhysicalCache pcc = new PhysicalClientCache();
        IPhysicalCache pcc = new CaffeineClientCache();
        ClientCache cache = new ClientCache(pcc);

        Client ret = new Client(true, cache);
        VirtualCenterInClient virtualCenterInClient = new VirtualCenterInClient(ret){
            public ICacheData get(String compKey) {
                IClientCacheData cacheData = (IClientCacheData) super.get(compKey);
                return cacheData == null ? null : cacheData.clone();
            }
        };
        virtualCenterInClient.setCenter(center);
//        virtualCenterInClient.registerClient(ret.getName(), ret);

        cache.setClient(ret);
        return ret;
    }
}

class Worker<T> implements Runnable {
    ITask task;
    T target;

    public Worker(T target) {
        this.target = target;
    }

    T getTarget() {
        return target;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (task != null) {
                try {
                    task.exec();
                } catch (ServiceCallException e) {
                    throw new RuntimeException(e);
                }
                task = null;
            }
        }
    }

    void setTask(ITask task) {
        this.task = task;
        synchronized (this) {
            this.notify();
        }
    }

    boolean isFinished() {
        synchronized (this) {
            return task == null;
        }
    }
}

@FunctionalInterface
interface ITask {
    void exec() throws ServiceCallException;
}