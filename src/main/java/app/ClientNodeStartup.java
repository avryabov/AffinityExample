package app;

import java.util.Scanner;
import model.Company;
import model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import service.CompanyCacheService;
import service.PersonCacheService;
import service.TaskService;

public class ClientNodeStartup {

    private static String title = "Available commands:" +
        "\ndemo" +
        "\nadd [cache] [id] [name]" +
        "\nget [cache] [id]" +
        "\nrun|affRun|call|affCall + [id]" +
        "\nstat [cache] [command]" +
        "\n" +
        "\ncache   - com|per" +
        "\nid      - if per cache then id format - [perId] [comId]" +
        "\ncommand - run|affRun|call|affCall";

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        IgniteConfiguration cfg = new IgniteConfiguration();
        Ignite ignite = Ignition.start(cfg);

        System.out.println("Client node has connected to the cluster");

        System.out.println(title);

        ignite.createCache("persons");
        ignite.createCache("companies");

        CompanyCacheService comCacheService = new CompanyCacheService(ignite);
        PersonCacheService perCacheService = new PersonCacheService(ignite);
        TaskService taskService = new TaskService(comCacheService, perCacheService);

        comCacheService.add(1, new Company("ComName1"));
        comCacheService.add(2, new Company("ComName2"));
        perCacheService.add(1, 1, new Person("PerName1"));
        perCacheService.add(2, 1, new Person("PerName2"));
        perCacheService.add(3, 2, new Person("PerName3"));

        Scanner scaner = new Scanner(System.in);
        while (true) {
            System.out.println("\nEnter command:");
            String str = scaner.nextLine();
            if (str.equals(""))
                break;
            String result = taskService.doTask(str);
            System.out.println(result);
        }
    }
}
