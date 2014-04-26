package edu.sjsu.cmpe.cache.client;

import com.google.common.hash.Hashing;
import org.soqqo.datagen.RandomDataGenerator;
import org.soqqo.datagen.config.DataTypes;
import org.soqqo.datagen.config.GenConfig;

import java.util.ArrayList;
import java.util.List;

public class Client {


    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");

        List<CacheServiceInterface> nodesList = new ArrayList<CacheServiceInterface>();
        nodesList.add(new DistributedCacheService("http://localhost:3000"));
        nodesList.add(new DistributedCacheService("http://localhost:3001"));
        nodesList.add(new DistributedCacheService("http://localhost:3002"));

        ConsistentHash<CacheServiceInterface> consistentHash = new ConsistentHash<CacheServiceInterface>(Hashing.md5(), 100, nodesList);

        RandomDataGenerator rdg = new RandomDataGenerator();
        List<Person> randomPersons = rdg.generateList(10, new GenConfig().name(DataTypes.Name.Firstname, "name"), Person.class);

        int i = 1;

        System.out.println("\nBEGIN INSERTION:");
        for (Person p : randomPersons){
            consistentHash.findNodeForAnObject(i).put(i, p.getName() );
            i ++;
        }


        System.out.println("\nTOTALS:");
        for (CacheServiceInterface csi : nodesList){
            System.out.println("Cache Server [" + csi.getCacheServerUrl() + "] has [" + csi.getCount() + "] records");
        }

        System.out.println("\nDETAILS:");
        int j = 1;
        for (Person p : randomPersons){
            System.out.println("ID " + j + " is in " + consistentHash.findNodeForAnObject(j).getCacheServerUrl());
            j++;
        }





        System.out.println("Existing Cache Client...");
    }


}
