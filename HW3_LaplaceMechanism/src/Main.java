import java.io.*;
import java.util.*;
import java.math.*;

public class Main {
    public record agegender(int age, int gender){};

    // takes data from csv file and converts it into a map counting every unique age and gender
    public static Map<agegender, Integer> readCSV(String csvFile){
        Map<agegender, Integer> dataMap = new HashMap<>();

        // writes default value of 0 for every age and gender
        for (int age = 17; age <= 90; age++){
            for (int gender = 1; gender <= 2; gender++){
                agegender key = new agegender(age, gender);
                dataMap.put(key, 0);
            }
        }

        // reads file and writes data to Map
        String line = "";
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            line = br.readLine();
            line = br.readLine();



            while(line!=null){
                String[] tokens = line.split(",");
                int age = Integer.parseInt(tokens[0]);
                int gender = Integer.parseInt(tokens[1]);
                agegender key = new agegender(age, gender);
                dataMap.merge(key, 1, Integer::sum);

                line = br.readLine();
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return dataMap;
    }


    // takes the Map of original data and returns a new Map with noise added
    public static Map<agegender, Integer> generateNoise(Map<agegender, Integer> dataMap, double epsilon){
        Map<agegender, Integer> noiseMap = new HashMap<>();

        double noise;
        int noiseRounded;
        for(agegender key : dataMap.keySet()){
            noise = laplaceValue(epsilon) + dataMap.get(key);
            
            if (noise<=0){
                noiseRounded = 0;
            } else {
                noiseRounded = (int) Math.round(noise);
                
            }

            noiseMap.merge(key, noiseRounded, Integer::sum);
        }

        return noiseMap;
    }


    // returns a random value from the laplace distribution centered at 0
    public static double laplaceValue(double epsilon){
        double laplaceRandom;
        double b = 1/Math.abs(epsilon);
        double u = Math.random()-.5;

        laplaceRandom = -b *Math.signum(u) * Math.log(1-2 * Math.abs(u));

        return laplaceRandom;
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter privacy budget ϵ:");
        double privacyBudget = scanner.nextDouble();


        // calculates MAE
        /*    --- Main assignment ---

        // Original data from CSV file
        Map<agegender,Integer> OriginalData = readCSV("adult_age_gender_dataset.csv");

        // Data with noise added to it
        Map<agegender,Integer> noisyData = generateNoise(OriginalData, privacyBudget);



        double MAE = 0;
        for(agegender key : OriginalData.keySet()){
            int bin = OriginalData.get(key);
            int noiseBin = noisyData.get(key);
            int difference = bin - noiseBin;
            System.out.println(bin);
            System.out.println(noiseBin);
            MAE+=Math.abs(difference);
        }
        MAE = MAE/OriginalData.size();
        System.out.println(MAE);
        */


        //    --- Extra credit ---
        for(int i =0; i<10; i++){
            // Original data from CSV file
            Map<agegender,Integer> OriginalData = readCSV("adult_age_gender_dataset.csv");

            // Data with noise added to it
            Map<agegender,Integer> noisyData = generateNoise(OriginalData, privacyBudget);
            agegender maxKey = null;
            agegender maxNoisyKey = null;
            int nMax = 0;
            int oMax = 0;
            for(agegender key : OriginalData.keySet()){
                if (OriginalData.get(key)>oMax){
                    maxKey = key;
                }
            }
            for(agegender key : noisyData.keySet()){
                if (noisyData.get(key)>nMax){
                    maxNoisyKey = key;
                }
            }

            System.out.println("Test: "+(i+1));
            System.out.println(maxKey);
            System.out.println(maxNoisyKey);
            System.out.println("-----------------------------------");
        }

    }
}