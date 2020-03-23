package NAI_1;

public class Iris {

    public String name;
    public double[] attributes;

    public Iris(String line){

        String[] splitValues = line.split("\\t");

        int countAttributes = splitValues.length - 1;
        attributes = new double[countAttributes];


        for (int i = 0; i < countAttributes; i++) {
            attributes[i] = Double.parseDouble(splitValues[i].replace(",", "."));
        }

        name = splitValues[countAttributes];
    }

    public Iris(double[] attributes){
        this.attributes = attributes;
    }
}