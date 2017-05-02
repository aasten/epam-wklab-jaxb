package com.epam.training;

import https.github_com.aasten.epam_wklab_jaxb.ObjectFactory;
import https.github_com.aasten.epam_wklab_jaxb.Product;
import https.github_com.aasten.epam_wklab_jaxb.Products;
import org.apache.commons.cli.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.PrintStream;

/**
 * Created by lik on 02.05.17.
 */
public class Main {

    // For usage print
    private static final String UTILITY_NAME = "jaxb-serializer";

    private static JAXBContext JAXBCONTEXT = null;
    static {
        try {
            JAXBCONTEXT = JAXBContext.newInstance(Products.class);
        } catch (JAXBException e) {
            System.err.println("JAXBContext not instantiated: " + e);
        }
    }

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private static class InOut {
        private String inFileName;
        private String outFileName;

        public void setInFileName(String inFileName) {
            this.inFileName = inFileName;
        }
        public void setOutFileName(String outFileName) {
            this.outFileName = outFileName;
        }
        public String getInFileName() {
            return inFileName;
        }
        public String getOutFileName() {
            return outFileName;
        }
    }

    private static class GetInOut {
        public static InOut getFromCommandLineArgs(String[] args) {
            Options options = new Options();

            Option input = new Option("i", "input", true, "input file path");
            input.setRequired(true);
            options.addOption(input);

            Option output = new Option("o", "output", true, "output file path");
            output.setRequired(true);
            options.addOption(output);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                formatter.printHelp(UTILITY_NAME, options);
                throw new RuntimeException(e);
            }

            InOut inOut = new InOut();
            inOut.setInFileName(cmd.getOptionValue("input"));
            inOut.setOutFileName(cmd.getOptionValue("output"));
            return inOut;
        }
    }

    public static void main(String[] args) {
        try {
            InOut inOut = GetInOut.getFromCommandLineArgs(args);
            Products unmarshalled = getUnmarshalled(new File(inOut.getInFileName()));
            printProducts(unmarshalled, System.out);
            marshalToFile(unmarshalled, new File(inOut.getOutFileName()));
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static Products getUnmarshalled(File file) {
        try {
            return (JAXBCONTEXT.createUnmarshaller().unmarshal(new StreamSource(file), Products.class))
                    .getValue();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private static void marshalToFile(Products p, File file) {
        try {
            // this might be moved to a private member of type JAXBContext and shared between this method and
            // getUnmarshalled(), but kept here to short the source file
            Marshaller m = JAXBCONTEXT.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(OBJECT_FACTORY.createProducts(p), file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void printProducts(Products products, PrintStream out) {
        out.println("Products:");
        final String tab = "\t";
        for(Product p : products.getProduct()) {
            out.println(tab + p.getName());
            out.println(tab + tab + "Price: " + p.getPrice());
            out.println(tab + tab + "Amount: " + p.getAmount());
            out.println(tab + tab + "Description: " + p.getDescription());
            out.println(tab + tab + "Type: " + p.getType());
        }
    }
}
