package service;

import java.util.HashMap;
import java.util.Map;
import model.Person;
import model.PersonKey;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

public class PersonCacheService {

    private Ignite ignite;

    private IgniteCache<PersonKey, Person> perCache;

    private Map<Integer, PersonKey> keyMap;

    public PersonCacheService(Ignite ignite) {
        this.ignite = ignite;
        perCache = ignite.cache("persons");
        keyMap = new HashMap<>();
    }

    public PersonKey add(Integer perId, Integer comId, Person per) {
        PersonKey perKey = new PersonKey(perId, comId);
        keyMap.put(perId, perKey);
        perCache.put(perKey, per);
        return perKey;
    }

    public Person get(Integer perId) {
        return perCache.get(keyMap.get(perId));
    }

    public void run(Integer perId) {
        ignite.compute().run((IgniteRunnable)() -> {
            Ignite ignite = Ignition.ignite();
            System.out.println("Run on " + ignite.cluster().localNode().id());
            System.out.println(ignite.cache("persons").get(keyMap.get(perId)));
        });
    }

    public void affinityRun(Integer perId) {
        ignite.compute().affinityRun("persons", perId, () -> {
            Ignite ignite = Ignition.ignite();
            System.out.println("AffinityRun on " + ignite.cluster().localNode().id());
            System.out.println(ignite.cache("persons").get(keyMap.get(perId)));
        });
    }

    public String call(Integer perId) {
        return ignite.compute().call((IgniteCallable<String>)() -> {
            Ignite ignite = Ignition.ignite();
            String str = "Run on " + ignite.cluster().localNode().id();
            String comStr = ignite.cache("persons").get(keyMap.get(perId)).toString();
            return str + "\n" + comStr;
        });
    }

    public String affinityCall(Integer perId) {
        return ignite.compute().affinityCall("persons", perId, () -> {
            Ignite ignite = Ignition.ignite();
            String str = "Run on " + ignite.cluster().localNode().id();
            String comStr = ignite.cache("persons").get(keyMap.get(perId)).toString();
            return str + "\n" + comStr;
        });
    }
}
