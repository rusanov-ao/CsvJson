import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);



        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        //System.out.println(json);

        try (FileWriter file = new FileWriter("new_data.json")) {
            file.write(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder1 = factory.newDocumentBuilder();
        Document doc = builder1.parse(new File("data.xml"));
        Node root = doc.getDocumentElement();

        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            //System.out.println(nodeList.item(i));
            Node node1 = nodeList.item(i);
//            Element element1 = (Element) node1;
//            List<Employee> list2 = (List<Employee>) element1.getAttributes();

            if (Node.ELEMENT_NODE == node1.getNodeType()) {
                Element element = (Element) node1;
                //List<Employee> list2 = element
                NamedNodeMap map = element.getAttributes();

                for (int j = 0; j < map.getLength(); j++) {
                    String attrName = map.item(j).getNodeValue();

                }
            }
        }


    }


    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> staff = csv.parse();
            //staff.forEach(System.out::println);
            return staff;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }



}
