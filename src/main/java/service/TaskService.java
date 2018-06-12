package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Company;
import model.Person;

public class TaskService {

    private CompanyCacheService comCacheService;
    private PersonCacheService perCacheService;
    private Map<String, List<Long>> mapStat;

    public TaskService(CompanyCacheService comCacheService, PersonCacheService perCacheService) {
        this.comCacheService = comCacheService;
        this.perCacheService = perCacheService;
        mapStat = new HashMap<>();
    }

    public String doTask(String str) {
        String[] arr = str.split(" ");
        if (arr.length == 0)
            return "ERROR in command";
        String result = null;
        String cmd = arr[0];
        if (cmd.equals("demo"))
            return demo();
        if (arr.length < 2)
            return "ERROR in command";
        String cache = arr[1];

        long stTime = System.nanoTime();
        switch (cmd) {
            case "add":
                result = addTak(arr);
                break;
            case "get":
                result = getTask(arr);
                break;
            case "run":
                result = runTask(arr);
                break;
            case "affRun":
                result = affRunTask(arr);
                break;
            case "call":
                result = callTask(arr);
                break;
            case "affCall":
                result = affCallTask(arr);
                break;
            case "stat":
                result = getStat(arr);
                break;
        }
        if (result != null) {
            long endTime = System.nanoTime();
            Long duration = endTime - stTime;
            String key = cache + " " + cmd;
            if (!mapStat.containsKey(key))
                mapStat.put(key, new ArrayList<>(Arrays.asList(duration)));
            else {
                List lst = mapStat.get(key);
                lst.add(duration);
                mapStat.put(key, lst);
            }
            result += "\nDurations: " + duration + " ns";
        }
        else
            return "ERROR in command";
        return result;
    }

    private String addTak(String[] arr) {
        switch (arr[1]) {
            case "com":
                return comCacheService.add(Integer.valueOf(arr[2]), new Company(arr[3])).toString();
            case "per":
                return perCacheService.add(Integer.valueOf(arr[2]), Integer.valueOf(arr[3]), new Person(arr[4])).toString();
            default:
                return null;
        }
    }

    private String getTask(String[] arr) {
        switch (arr[1]) {
            case "com":
                return comCacheService.get(Integer.valueOf(arr[2])).toString();
            case "per":
                return perCacheService.get(Integer.valueOf(arr[2])).toString();
            default:
                return null;
        }
    }

    private String runTask(String[] arr) {
        switch (arr[1]) {
            case "com":
                comCacheService.run(Integer.valueOf(arr[2]));
                return "";
            case "per":
                perCacheService.run(Integer.valueOf(arr[2]));
                return "";
            default:
                return null;
        }
    }

    private String affRunTask(String[] arr) {
        switch (arr[1]) {
            case "com":
                comCacheService.affinityRun(Integer.valueOf(arr[2]));
                return "";
            case "per":
                perCacheService.affinityRun(Integer.valueOf(arr[2]));
                return "";
            default:
                return null;
        }
    }

    private String callTask(String[] arr) {
        switch (arr[1]) {
            case "com":
                return comCacheService.call(Integer.valueOf(arr[2]));
            case "per":
                return perCacheService.call(Integer.valueOf(arr[2]));
            default:
                return null;
        }
    }

    private String affCallTask(String[] arr) {
        switch (arr[1]) {
            case "com":
                return comCacheService.affinityCall(Integer.valueOf(arr[2]));
            case "per":
                return perCacheService.affinityCall(Integer.valueOf(arr[2]));
            default:
                return null;
        }
    }

    private String getStat(String[] arr) {
        switch (arr[1]) {
            case "all":
                return getStat();
            default:
                return getStat(arr[1] + " " + arr[2]);
        }
    }

    private String getStat(String key) {
        List<Long> lst = mapStat.get(key);
        if (lst == null || lst.size() <= 0)
            return "None stat for" + key;
        Long sum = 0L;
        for (Long l : lst)
            sum += l;
        return "Stat " + key + ":" + div(key) + (sum / lst.size()) + " ns";
    }

    private String div(String key) {
        StringBuilder sb = new StringBuilder("");
        for (int i = key.length(); i <= 12; i++)
            sb.append(" ");
        return sb.toString();
    }

    private String getStat() {
        StringBuilder sb = new StringBuilder();
        List<String> lst = new ArrayList<>(mapStat.keySet());
        lst.sort(new Comparator<String>() {
            @Override public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (String s : lst)
            sb.append(getStat(s) + "\n");
        return sb.toString();
    }

    private String demo() {
        for (int i = 0; i < 10; i++) {
            doTask("run com 1");
            doTask("affRun com 1");
            doTask("call com 1");
            doTask("affCall com 1");
            doTask("run per 1");
            doTask("affRun per 1");
            doTask("call per 1");
            doTask("affCall per 1");
        }
        return getStat();
    }
}
