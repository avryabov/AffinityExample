package service;

import model.Company;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

public class CompanyCacheService {

    private Ignite ignite;

    private static IgniteCache<Integer, Company> comCache;

    public CompanyCacheService(Ignite ignite) {
        this.ignite = ignite;
        comCache = ignite.cache("companies");
    }

    public Integer add(Integer comId, Company com) {
        comCache.put(comId, com);
        return comId;
    }

    public Company get(Integer comId) {
        return comCache.get(comId);
    }

    public void run(Integer comId) {
        ignite.compute().run((IgniteRunnable)() -> {
            Ignite ignite = Ignition.ignite();
            System.out.println("Run on " + ignite.cluster().localNode().id());
            System.out.println(ignite.cache("companies").get(comId));
        });
    }

    public void affinityRun(Integer comId) {
        ignite.compute().affinityRun("companies", comId, () -> {
            Ignite ignite = Ignition.ignite();
            System.out.println("AffinityRun on " + ignite.cluster().localNode().id());
            System.out.println(ignite.cache("companies").get(comId));
        });
    }

    public String call(Integer comId) {
        return ignite.compute().call((IgniteCallable<String>)() -> {
            Ignite ignite = Ignition.ignite();
            String str = "Run on " + ignite.cluster().localNode().id();
            String comStr = ignite.cache("companies").get(comId).toString();
            return str + "\n" + comStr;
        });
    }

    public String affinityCall(Integer comId) {
        return ignite.compute().affinityCall("companies", comId, () -> {
            Ignite ignite = Ignition.ignite();
            String str = "Run on " + ignite.cluster().localNode().id();
            String comStr = ignite.cache("companies").get(comId).toString();
            return str + "\n" + comStr;
        });
    }

}
