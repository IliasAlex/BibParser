package bibparser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXParser;
import org.jbibtex.ParseException;

public class BibParser {

    public static FileWriter arffWriter;
    
    public static String[] arr = {"athanasiadou", "blionas", "glentis", "kaloxylos", "karagiorgos", "kolokotronis", "lepouras", "malamatos", "masselos", "moscholios", "peppas", "platis", "sagias", "skiadopoulos", "stavdas", "tryfonopoulos", "tselikas", "tsoulos", "vassilakis", "wallace", "yiannopoulos", "other"};

    public static void main(String[] args) throws IOException {
        File folder = new File("dit-uop-professors");

        arffWriter = new FileWriter(new File("output/DEP.arff"));
        arffWriter.write("@relation DEP\n");
        for (int i = 0; i < arr.length; i++) {
            arffWriter.write("@attribute " +arr[i] + " NUMERIC\n");
        }
        arffWriter.write("\n@data\n");

        for (File fileEntry : folder.listFiles()) {
            parse(fileEntry.getPath());
        }
        arffWriter.close();
    }

    public static void parse(String filename) throws IOException {
        String comma = ",";
        int id = 0;
        System.out.println(filename);
        File f_in = new File(filename);

        System.out.println("output/" + f_in.getName());
        File f_out = new File("output/" + f_in.getName());

        try {
            FileReader myReader = new FileReader(f_in);

            BibTeXParser bibtexParser = new BibTeXParser();
            BibTeXDatabase bib_data = bibtexParser.parse(myReader);

            Map<org.jbibtex.Key, org.jbibtex.BibTeXEntry> entryMap = bib_data.getEntries();

            Collection<org.jbibtex.BibTeXEntry> entries = entryMap.values();

            FileWriter myWriter = new FileWriter(f_out);

            for (org.jbibtex.BibTeXEntry entry : entries) {
                
                myWriter.write((++id)+",\"");
                String dblp = entry.getKey().toString();

                org.jbibtex.Value author = entry.getField(org.jbibtex.BibTeXEntry.KEY_AUTHOR);
                org.jbibtex.Value editors = entry.getField(org.jbibtex.BibTeXEntry.KEY_EDITOR);
                if (dblp!=null && author != null && editors != null) {
                    String a = author.toUserString().replace(" and", ",");
                    a = a.replace("\n", "");
                    a = replaceAuthor(a);
                    String e = editors.toUserString().replace(" and", ",");
                    e = e.replace("\n", "");
                    e = replaceAuthor(e);
                    comma = ",";
                    for(int i = 0; i < arr.length; i++){
                        if( i == arr.length-1){
                            comma = "\n";
                        }
                        if( a.contains(arr[i]) || e.contains(arr[i])){
                            arffWriter.write("1"+comma);
                        }
                        else{
                            arffWriter.write("0"+comma);
                        }
                    }
                    
                    if (e.isEmpty()) {
                        myWriter.write(dblp + "\"," + a + "\n");
                    } else {
                        myWriter.write(dblp + "\"," + a + "," + e + "\n");  
                    }
                } else if (!dblp.isEmpty() && editors != null) {
                    String e = editors.toUserString().replace(" and", ",");
                    e = e.replace("\n", "");
                    e = replaceAuthor(e);                    
                    myWriter.write(dblp + "\"," + e + "\n");
                    comma = ",";
                    for(int i = 0; i < arr.length; i++){
                        if( i == arr.length-1){
                            comma = "\n";
                        }
                        if( e.contains(arr[i])){
                            arffWriter.write("1"+comma);
                        }
                        else{
                            arffWriter.write("0"+comma);
                        }
                    }
                } else if (!dblp.isEmpty() && author != null) {
                    String a = author.toUserString().replace(" and", ",");
                    a = a.replace("\n", "");
                    a = replaceAuthor(a);
                    myWriter.write(dblp + "\"," + a + "\n");
                    comma = ",";
                    for(int i = 0; i < arr.length; i++){
                        if( i == arr.length-1){
                            comma = "\n";
                        }
                        if( a.contains(arr[i])){
                            arffWriter.write("1"+comma);
                        }
                        else{
                            arffWriter.write("0"+comma);
                        }
                    }
                }

            }
            myWriter.close();
            myReader.close();
        } catch (ParseException e) {
            System.err.println(e);
        }
    }
    
    public static String replaceAuthor(String str) {
        String parts[] = str.split(",");
        StringBuilder authors = new StringBuilder();
        String comma = ",";
        int i = 0;
        for (String part : parts) {
            i++;
            if (i == parts.length) {
                comma = "";
            }
            if (part.equalsIgnoreCase("Georgia E. Athanasiadou")) {
                authors.append("\"athanasiadou\"" + comma);
            } else if (part.equalsIgnoreCase("Spyros Blionas")) {
                authors.append("\"blionas\"" + comma);
            } else if (part.equalsIgnoreCase("George{-}Othon Glentis")) {
                authors.append("\"glentis\"" + comma);
            } else if (part.equalsIgnoreCase("Alexandros Kaloxylos")) {
                authors.append("\"kaloxylos\"" + comma);
            } else if (part.equalsIgnoreCase("Gregory Karagiorgos")) {
                authors.append("\"karagiorgos\"" + comma);
            } else if (part.equalsIgnoreCase("Nicholas Kolokotronis")) {
                authors.append("\"kolokotronis\"" + comma);
            } else if (part.equalsIgnoreCase("George Lepouras") || part.equalsIgnoreCase("Giorgos Lepouras") || part.equalsIgnoreCase("Georgios Lepouras")) {
                authors.append("\"lepouras\"" + comma);
            } else if (part.equalsIgnoreCase("Theocharis Malamatos")) {
                authors.append("\"malamatos\"" + comma);
            } else if (part.equalsIgnoreCase("Konstantinos Masselos") || part.equalsIgnoreCase("Kostas Masselos")) {
                authors.append("\"masselos\"" + comma);
            } else if (part.equalsIgnoreCase("Ioannis D. Moscholios")) {
                authors.append("\"moscholios\"" + comma);
            } else if (part.equalsIgnoreCase("Kostas P. Peppas") || part.equalsIgnoreCase("Kostas Peppas")) {
                authors.append("\"peppas\"" + comma);
            } else if (part.equalsIgnoreCase("Nikos Platis")) {
                authors.append("\"platis\"" + comma);
            } else if (part.equalsIgnoreCase("Nikos C. Sagias") || part.equalsIgnoreCase("Nikolaos C. Sagias")) {
                authors.append("\"sagias\"" + comma);
            } else if (part.equalsIgnoreCase("Spiros Skiadopoulos")) {
                authors.append("\"skiadopoulos\"" + comma);
            } else if (part.equalsIgnoreCase("Alexandros A. Stavdas")) {
                authors.append("\"stavdas\"" + comma);
            } else if (part.equalsIgnoreCase("Christos Tryfonopoulos")) {
                authors.append("\"tryfonopoulos\"" + comma);
            } else if (part.equalsIgnoreCase("Nikolaos D. Tselikas")) {
                authors.append("\"tselikas\"" + comma);
            } else if (part.equalsIgnoreCase("George V. Tsoulos") || part.equalsIgnoreCase("Georgios Tsoulos")) {
                authors.append("\"tsoulos\"" + comma);
            } else if (part.equalsIgnoreCase("Costas Vassilakis")) {
                authors.append("\"vassilakis\"" + comma);
            } else if (part.equalsIgnoreCase("Manolis Wallace")) {
                authors.append("\"wallace\"" + comma);
            } else if (part.equalsIgnoreCase("Kostas Yiannopoulos") || part.equalsIgnoreCase("Konstantinos Yiannopoulos")) {
                authors.append("\"yiannopoulos\"" + comma);
            } else{
                authors.append("\"other\"" + comma);
            }
        }
        if (authors.toString().endsWith(",")) {
            return authors.toString().substring(0, authors.toString().length() - 1);
        }
        return authors.toString();
    }
}
