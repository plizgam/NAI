package NAI_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Perception {

    double learnValue = 0.7, thetha = 1;
    double[] weights;
    int correctSuccess, countLearning = 10;

    public Perception(String trainFile, String testFile) throws IOException {
        ArrayList<Iris> trainIrises = readIrisFromFile(trainFile);
        ArrayList<Iris> testIrises = readIrisFromFile(testFile);
        weights = new double[trainIrises.get(0).attributes.length];


        for (int i = 0; i < weights.length; i++)
            weights[i] = new Random().nextDouble();


        analyseData(trainIrises);
        testDataSet(testIrises);

        System.out.println();

        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("Czy chcesz wprowadzić nowe atrybuty?");

            int countAttributes = trainIrises.get(0).attributes.length;
            double[] myAttributes = new double[countAttributes];

            if(scan.next().equals("tak")){

                for (int i = 0; i < countAttributes; i++) {
                    System.out.println("Wprowadź " + (i+1) + ". atrybut:");
                    myAttributes[i] = scan.nextDouble();
                }

                Iris myIris = new Iris(myAttributes);
                ArrayList<Iris> myOwn = new ArrayList<Iris>();
                myOwn.add(myIris);
                testDataSet(myOwn);
            }else{
                break;
            }

        }
    }

    public ArrayList<Iris> readIrisFromFile(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        ArrayList<Iris> irises = new ArrayList<>();

        while (scanner.hasNextLine())
            irises.add(new Iris(scanner.nextLine()));

        return irises;
    }
    public void analyseData(ArrayList<Iris> trainIrises){

        while(countLearning-- >= 0) {
            for (Iris iris : trainIrises)
                deltaCalc(iris);
        }
    }



    public void deltaCalc(Iris iris){
        double sum = 0.0;

        if(iris.name.trim().equals("Iris-setosa")){
            sum = sumAttribute(iris);

            while(sum == 0){
                for (int i = 0; i < weights.length; i++) {
                    weights[i] = weights[i] + (1.0 - sum) * learnValue * iris.attributes[i];
                    thetha = weights[i] + (1.0 - sum) * learnValue * 1;
                }

                sum = sumAttribute(iris);
            }
        }else{
            sum = sumAttribute(iris);
            while(sum == 1){
                for (int i = 0; i < weights.length; i++) {
                    weights[i] = weights[i] + (0.0 - sum) * learnValue * iris.attributes[i];
                    thetha = weights[i] + (0.0 - sum) * learnValue * 1;
                }
                sum = sumAttribute(iris);
            }
        }
    }


    public double sumAttribute(Iris iris) {
        double sum = 0.0;
        for (int i = 0; i < iris.attributes.length; i++)
            sum += iris.attributes[i] * weights[i];

        if (sum >= thetha)
            return 1.0;
        else
            return 0.0;
    }

    public void testDataSet(ArrayList<Iris> testIrises) {
        int success = 0;


        for (Iris testIris : testIrises) {
            if (testIrises.size() > 1 && testIris.name.trim().equals("Iris-setosa"))
                correctSuccess++;


            if (sumAttribute(testIris) == 1) {
                if (testIrises.size() == 1)
                System.out.println("To jest Iris-Setosa");
                success++;
            } else {
                if (testIrises.size() == 1)
                System.out.println("To nie jest Iris-Setosa");
            }
        }

        if(testIrises.size() > 1)
        System.out.println("Wynik klasyfikacji: " + (success / correctSuccess) * 100 + "%");
    }
}