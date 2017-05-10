package com.thoughtworks.pact.provider;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import com.thoughtworks.Application;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@RunWith(PactRunner.class)
@Provider("bulb")
@PactFolder("pacts")
public class BulbProviderContractTest {

    private static ConfigurableApplicationContext application;

    @BeforeClass
    public static void startSpring(){
        application = new SpringApplicationBuilder()
                .profiles("integration")
                .sources(Application.class)
                .run();
    }

    @AfterClass
    public static void kill(){
        application.stop();
    }

    @TestTarget
    public final Target target = new HttpTarget(8080);

    @State("default state")
    public void toDefaultState() {

    }

    @State("no bulbs yet available")
    public void toEmptyBulbState() {
        System.out.println("no bulbs yet available");
    }

    @State("one basic bulb available")
    public void toOneBasicBulbAvailable() {
        System.out.println("one basic bulb available");
    }

}
