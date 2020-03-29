package NAI_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Knn {


    public Knn(String trainFile, String testFile) throws IOException {
        ArrayList<Iris> trainIrises = readIrisFromFile(trainFile);
        ArrayList<Iris> testIrises = readIrisFromFile(testFile);
        LinkedHashSet<String> types = new LinkedHashSet<>();

        for (Iris iris : trainIrises)
            types.add(iris.name);

        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.println("Wprowadź liczbę K lub wpisz 'N', aby dodać atrybuty");
            if (scan.hasNextInt()) {
                analyseData(scan.nextInt(), trainIrises, testIrises, types);
            } else{
                scan.next();
                System.out.println("Wprowadź K");
                int k = scan.nextInt();
                int countAttributes = trainIrises.get(0).attributes.length;
                double[] attributes = new double[countAttributes];

                for (int i = 0; i < countAttributes; i++) {
                    System.out.println("Wprowadź " + (i+1) + ". atrybut" );
                    attributes[i] = scan.nextDouble();
                }
                newTest(k, trainIrises, new Iris(attributes), types);
            }

            System.out.println("\n");
        }
    }

    public ArrayList<Iris> readIrisFromFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        ArrayList<Iris> irises = new ArrayList<>();

        while (scanner.hasNextLine())
            irises.add(new Iris(scanner.nextLine()));

        return irises;
    }

    public void analyseData(int k, ArrayList<Iris> trainIrises, ArrayList<Iris> testIrises, LinkedHashSet<String> types){
        if(k > trainIrises.size()){
            System.out.println("K większe od ilości danych. Wprowadź ponownie.");
            return;
        }

        int countSuccess = 0;
        for (Iris iris : testIrises) {
            ArrayList<Neighbour> neighbours = new ArrayList<>();

            for (Iris irisTrain : trainIrises) {

                int countAttributes = irisTrain.attributes.length;
                double[] values = new double[countAttributes];
                double sum = 0;

                for (int i = 0; i < countAttributes; i++)
                    sum += Math.pow(iris.attributes[i] - irisTrain.attributes[i], 2);


                neighbours.add(new Neighbour(irisTrain.name, Math.sqrt(sum)));
            }

            Collections.sort(neighbours, Comparator.comparingDouble(o -> o.distance));

            int theBiggestGroup = 0;
            String BestGroup = "";
            for (String type : types) {

                int countElements = 0;
                for (int i = 0; i < k; i++) {


                   if(type.contains(neighbours.get(i).name))
                    countElements++;

                }

                if(theBiggestGroup <= countElements) {
                    theBiggestGroup = countElements;
                    BestGroup = type;
                }
            }

            if(iris.name.contains(BestGroup))
                countSuccess++;

        }

        System.out.println("Zaklasyfikowano: " + countSuccess);
        System.out.println("Skuteczność: " + (countSuccess*100)/testIrises.size() + "%");

    }

    public void newTest(int k, ArrayList<Iris> trainIrises, Iris testIris, LinkedHashSet<String> types){
        if(k > trainIrises.size()){
            System.out.println("K większe od ilości danych. Wprowadź ponownie.");
            return;
        }


        ArrayList<Neighbour> neighbours = new ArrayList<>();
        for (Iris irisTrain : trainIrises) {

            int countAttributes = irisTrain.attributes.length;
            double[] values = new double[countAttributes];
            double sum = 0;

            for (int i = 0; i < countAttributes; i++)
                sum += Math.pow(testIris.attributes[i] - irisTrain.attributes[i], 2);


            neighbours.add(new Neighbour(irisTrain.name, Math.sqrt(sum)));
        }

        Collections.sort(neighbours, Comparator.comparingDouble(o -> o.distance));

        int theBiggestGroup = 0;
        String BestGroup = "";
        for (String type : types) {

            int countElements = 0;
            for (int i = 0; i < k; i++) {


                if(type.contains(neighbours.get(i).name))
                    countElements++;

            }

            if(theBiggestGroup <= countElements) {
                theBiggestGroup = countElements;
                BestGroup = type;
            }
        }


        System.out.println("Zaklasyfikowano do " + BestGroup);
    }
}