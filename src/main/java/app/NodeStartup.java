package app;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class NodeStartup {
    public static void main(String[] args) {
        IgniteConfiguration cfg = new IgniteConfiguration();
        Ignition.start(cfg);
    }
}
