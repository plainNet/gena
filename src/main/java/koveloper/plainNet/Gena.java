/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koveloper.plainNet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kgn
 */
public class Gena {

    private static String intro = "Hello, I'm Gena!\n"
            + "My function is recycling your web project files \n"
            + "into C-style arrays placed into FLASH (.text) memory.\n"
            + "The result will be placed in \"HttpHostDefaultResources.cpp\".\n"
            + "\n"
            + "My command line arguments are listed below:\n"
            + "\n"
            + "- web_folder:\t\tdefines location of folder with web \n"
            + "\t\t\tproject files (see example below);\n"
            + "	\n"
            + "- out:\t\t\tdefines ABSOLUTE path for folder(s) where \n"
            + "\t\t\tresult file(s) will be placed (see example below);\n"
            + "	\n"
            + "- folder_ignore:\tdefines list of ignored folders inside \n"
            + "\t\t\t\"web_folder\" (see example below);\n"
            + "	\n"
            + "- file_ignore:\t\tdefines list of ignored files\n"
            + "\t\t\tinside \"web_folder\" (see example below);\n"
            + "	\n"
            + "- folder_ignore_src:\tdefines RELATIVE path to list-file with \n"
            + "\t\t\tenumeration ignored folders inside \n"
            + "\t\t\t\"web_folder\" (see example below);\n"
            + "	\n"
            + "- file_ignore_src:\tdefines RELATIVE path to list-file with \n"
            + "\t\t\tenumeration of ignored files inside\n"
            + "\t\t\t\"web_folder\" (see example below)."
            + "\n\n***** EXAMPLES CONDITIONS ******\n"
            + "\n"
            + "1) We have embedded firmware project. Project uses plainNet and it's HttpHost unit.\n"
            + "   option PLAINNET_USE_DEFAULT_HTTP_RESOURCES = 1, so HttpHostDefaultResources unit \n"
            + "   is used.\n"
            + "2) Embedded project sources are placed in \"/opt/hardware/stm32/projects/em-proj\" folder.\n"
            + "   Sources of plainNet are placed in plainNet/src folder inside project folder and includes\n"
            + "   in plainNet/inc.\n"
            + "   \n"
            + "/opt/hardware/stm32/projects/em-proj\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> ...\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> plainNet\t*--> inc\t*--> HttpHostDefaultResources.h\n"
            + "\t\t\t\t\t\t|\t\t|\n"
            + "\t\t\t\t\t\t|\t\t*--> ...\n"
            + "\t\t\t\t\t\t|\n"
            + "\t\t\t\t\t\t*--> src\t*--> HttpHostDefaultResources.cpp (!!!)\n"
            + "\t\t\t\t\t\t\t\t|\n"
            + "\t\t\t\t\t\t\t\t*--> ...\n"
            + "\n"
            + "3) Web project of our HttpServer resources is placed in \"/opt/hardware/stm32/projects/em-proj-web\".\n"
            + "   Inside this folder we have common web project folders structure\n"
            + "\n"
            + "/opt/hardware/stm32/projects/em-proj-web\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> .git\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> README.md\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> index.html\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> js (folder)\t*-->script_1.js\n"
            + "\t\t\t\t|\t\t\t|\n"
            + "\t\t\t\t|\t\t\t*-->script_2.js\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> css (folder)\t*--> styles.css\n"
            + "\t\t\t\t|\n"
            + "\t\t\t\t*--> less (folder)\t*--> header.less\n"
            + "\t\t\t\t\t\t\t|\n"
            + "\t\t\t\t\t\t\t*--> fonts.less\n"
            + "\t\t\t\t\t\t\t|\n"
            + "\t\t\t\t\t\t\t*--> styles.less\n"
            + "\n"
            + "4) Gena is used to map all allowded resources in \"/opt/hardware/stm32/projects/em-proj-web\"\n"
            + "   into \"HttpHostDefaultResources.cpp\" as bytes arrays placed into FLASH (.text) memory.\n"
            + "	\n"
            + "5) Folder \"/opt/hardware/stm32/projects/em-proj-web\" contains files and folder that \n"
            + "   must be removed from HttpServer unit resources on \"HttpHostDefaultResources.cpp\"\n"
            + "   generate.\n"
            + "\n"
            + "***** EXAMPLES ******\n"
            + "\n"
            + "1) Using only comand line parameters:\n"
            + "\n"
            + "   java -jar /path-to-gena/Gena.jar web_folder=\"/opt/hardware/stm32/projects/em-proj-web\" out=\"/opt/hardware/stm32/projects/em-proj/plainNet/src\" folder_ignore=less file_igonre=*.git,*.md\n"
            + "	\n"
            + "2) Using external ignore files\n"
            + "\n"
            + "   java -jar /path-to-gena/Gena.jar web_folder=\"/opt/hardware/stm32/projects/em-proj-web\" out=\"/opt/hardware/stm32/projects/em-proj/plainNet/src\" folder_ignore_src=dir_igonre.ini file_ignore_src=file_igonre.ini\n"
            + "\n"
            + "	**** dir_igonre.ini ****\n"
            + "	*		       *\n"
            + "	*	less           *\n"
            + "	*		       *\n"
            + "	************************\n"
            + "\n"
            + "	**** file_igonre.ini ****\n"
            + "	*			*\n"
            + "	*	*.git		*\n"
            + "	*	*.md		*\n"
            + "	*			*\n"
            + "	*************************\n"
            + "	\n"
            + "In all examples Gena will ignore folder named \"less\" and all files in all directories with extensions *.git \n"
            + "and *.md. C-style arrays will be generated for:\n"
            + "	- index.html (URI: / and /index.html);\n"
            + "	- script_1.js (URI: /js/script_1.js);\n"
            + "	- script_2.js (URI: /js/script_2.js);\n"
            + "	- styles.css (URI: /css/styles.css);";
    
    public static String getMyFolder() {
        String path = (new File(Gena.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParent().replace("%20", " ");
        if (path.endsWith(System.getProperty("file.separator") + "build")) {
            path = path.replace(System.getProperty("file.separator") + "build", "");
        }
        return path + System.getProperty("file.separator");
    }
    
    private static void fillIgnore(LinkedList<String> ignoreList, File file) {
        if(!file.exists() || file.isDirectory()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null) {
                ignoreList.add(line);
            }
            reader.close();
        } catch (Exception e) {
        }
    }
    
    private static void fillIgnore(LinkedList<String> ignoreList, String ignoreRools) {
        if (ignoreRools != null) {
            if (ignoreRools.contains(",")) {
                for (String s : ignoreRools.split(",")) {
                    ignoreList.add(s);
                }
            } else {
                ignoreList.add(ignoreRools);
            }
        }
    }

    public static void main(String[] args) {

        String web_folder = null;
        String out = null;
        String folder_ignore = null;
        String file_ignore = null;
        String folder_ignore_src = null;
        String file_ignore_src = null;
        for (String arg : args) {
            if (arg.startsWith("web_folder=")) {
                web_folder = arg.replace("web_folder=", "");
            } else if (arg.startsWith("out=")) {
                out = arg.replace("out=", "");
            } else if (arg.startsWith("folder_ignore=")) {
                folder_ignore = arg.replace("folder_ignore=", "");
            } else if (arg.startsWith("file_ignore=")) {
                file_ignore = arg.replace("file_ignore=", "");
            } else if (arg.startsWith("folder_ignore_src=")) {
                folder_ignore_src = arg.replace("folder_ignore_src=", "");
            } else if (arg.startsWith("file_ignore_src=")) {
                file_ignore_src = arg.replace("file_ignore_src=", "");
            }
        }
        final LinkedList<String> ignoreFolders = new LinkedList<>();
        final LinkedList<String> ignoreFiles = new LinkedList<>();
        fillIgnore(ignoreFolders, folder_ignore);
        fillIgnore(ignoreFiles, file_ignore);
        if(folder_ignore_src != null) {
            fillIgnore(ignoreFolders, new File(folder_ignore_src));            
        }
        if(file_ignore_src != null) {
            fillIgnore(ignoreFiles, new File(file_ignore_src));            
        }
        if(web_folder == null || out == null) {
            System.out.println(intro);
            return;
        }
        try {
            Gena gena = new Gena();
            byte[] result = gena.encode(new File(web_folder), new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        for(String ignore : ignoreFolders) {
                            if(pathname.getAbsolutePath().endsWith(ignore)) {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        for(String ignore : ignoreFiles) {
                            if(ignore.startsWith("*.")) {
                                if(pathname.getAbsolutePath().endsWith(ignore.substring(1))) {
                                    return false;
                                }
                            } else {
                                if(pathname.getAbsolutePath().endsWith(ignore)) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                }
            });
            File out_ = new File(out, "HttpHostDefaultResources.cpp");
            System.out.println("Saving result to: " + out_.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(out_);
            fos.write(result);
            fos.flush();
            fos.close();
            System.out.println("Server resources volume is: " + gena.totalVolume + " bytes");
            System.out.println("Gena finished his job");        
        } catch (IOException ex) {
            Logger.getLogger(Gena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Gena() throws IOException {
        
    }
    
    private long totalVolume = 0;
    
    private byte[] encode(File webFolder, FileFilter accepter) throws IOException {
        System.out.println("Gena is starting...");
        totalVolume = 0;
        File[] files = webFolder.listFiles(accepter);
        LinkedList<File> burnFiles = new LinkedList<>();
        for (File f : files) {
            if (f.isFile()) {
                burnFiles.add(f);
            } else {
                LinkedList<File> inFolder = new LinkedList<>();
                extractFiles(f, inFolder, accepter);
                for (File inFolderFile : inFolder) {
                    burnFiles.add(inFolderFile);
                }
            }
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        result.write(("/*\n"
                + " * HttpHostDefaultResources.cpp\n"
                + " *\n"
                + " *  Created on: " + new Date(System.currentTimeMillis()).toString() + "\n"
                + " *  Author: Gena\n"
                + " */\n\n").getBytes(Charset.forName("UTF-8")));
        result.write(("#include <HttpHostDefaultResources.h>\n"
                + "\n"
                + "\n"
                + "#if PLAINNET_USE_DEFAULT_HTTP_RESOURCES == 1\n\n").getBytes(Charset.forName("UTF-8")));
        String function = "kvpr::network::HttpHostResources* plainnet_getDefaultHttpResources() {\n"
                + "\tkvpr::network::HttpHostResources* defaultResources = new kvpr::network::HttpHostResources();";
        int resourceCounter = 0;
        for (File file : burnFiles) {
            String uri = file.getAbsolutePath().replace(webFolder.getAbsolutePath(), "").replace("\\", "/");
            System.out.println(uri + " ---> c-style array volume is " + file.length() + " bytes");
            totalVolume += file.length();
            String array = toCppArray(file, 50);
            if (array != null) {
                String resourceName = "___plainNetHttpServerResource_" + resourceCounter++ + "___";
                String resourceContentType = getResourceContentType(file);
                array = "/*\n"
                        + " * " + uri + " ---> " + file.length() + " bytes"  + "\n"
                        + " */\n" + "static const unsigned char " + resourceName + "[] = " + array + ";\n\n";
                result.write(array.getBytes(Charset.forName("UTF-8")));

                function += "\n\tdefaultResources->add("
                        + "\"" + uri + "\", "
                        + resourceContentType + ", "
                        + "(uint8_t*) " + resourceName + ", "
                        + "sizeof(" + resourceName + "));";
                if (uri.endsWith("index.html")) {
                    function += "\n\tdefaultResources->add("
                            + "\"" + uri.replace("index.html", "") + "\", "
                            + resourceContentType + ", "
                            + "(uint8_t*) " + resourceName + ", "
                            + "sizeof(" + resourceName + "));";

                }
                if (uri.endsWith("index.shtml")) {
                    function += "\n\tdefaultResources->add("
                            + "\"" + uri.replace("index.shtml", "") + "\", "
                            + resourceContentType + ", "
                            + "(uint8_t*) " + resourceName + ", "
                            + "sizeof(" + resourceName + "));";

                }
            }
        }
        function += "\n\treturn defaultResources;\n"
                + "}\n"
                + "\n"
                + "#endif /* PLAINNET_USE_DEFAULT_HTTP_RESOURCES */";
        result.write(function.getBytes(Charset.forName("UTF-8")));
        return result.toByteArray();
    }

    private String getResourceContentType(File resource) {
        if (resource.getName().toLowerCase().endsWith("html")) {
            return "kvpr::network::HttpHostResponseType::_html_";
        } else if (resource.getName().toLowerCase().endsWith("shtml")) {
            return "kvpr::network::HttpHostResponseType::_html_";
        } else if (resource.getName().toLowerCase().endsWith("css")) {
            return "kvpr::network::HttpHostResponseType::_css_";
        } else if (resource.getName().toLowerCase().endsWith("js")) {
            return "kvpr::network::HttpHostResponseType::_javascript_";
        } else if (resource.getName().toLowerCase().endsWith("png")) {
            return "kvpr::network::HttpHostResponseType::_img_png_";
        } else if (resource.getName().toLowerCase().endsWith("ico")) {
            return "kvpr::network::HttpHostResponseType::_img_xicon_";
        } else if (resource.getName().toLowerCase().endsWith("jpg") || resource.getName().toLowerCase().endsWith("jpeg")) {
            return "kvpr::network::HttpHostResponseType::_img_jpeg_";
        } else if (resource.getName().toLowerCase().endsWith("tiff")) {
            return "kvpr::network::HttpHostResponseType::_img_tiff_";
        } else if (resource.getName().toLowerCase().endsWith("svg")) {
            return "kvpr::network::HttpHostResponseType::_img_svg_";
        } else if (resource.getName().toLowerCase().endsWith("json")) {
            return "kvpr::network::HttpHostResponseType::_json_";
        }
        return "kvpr::network::HttpHostResponseType::_octet_stream_";
    }

    private String toCppArray(File f, int maxSymbolsInRow) throws IOException {
        if (f.length() == 0) {
            return null;
        }
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        arr.write("{\r\n\t".getBytes());
        int inRow = 0;
        byte[] buf = new byte[4096];
        int n = 0;
        long length = f.length();
        long handled = 0;
        int lastPercent = 0;
        int percent = 0;
        while ((n = fis.read(buf)) != -1) {
            for (int i = 0; i < n; i++) {
                int sym = ((int) buf[i]) & 255;
                arr.write(("0x" + (sym <= 15 ? "0" : "") + Integer.toHexString(sym).toUpperCase() + ", ").getBytes());
                inRow++;
                if (inRow == maxSymbolsInRow) {
                    inRow = 0;
                    arr.write("\r\n\t".getBytes());
                }
            }
            if(handled == 0) {
                System.out.print("\tencoding: ");
                System.out.print("0%");
            }
            handled += n;
            percent = (int)(handled * 100 / length);
            if((percent - lastPercent) >= 5) {
                lastPercent = percent;
                System.out.print(" " + percent + "%");
            }
        }
        System.out.print("\t100%");
        System.out.println();
        arr.write("\r\n}".getBytes());
        return new String(arr.toByteArray());
    }

    private void extractFiles(File folder, LinkedList<File> to, FileFilter accepter) {
        if (!folder.isDirectory()) {
            return;
        }
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                if (accepter.accept(f)) {
                    to.add(f);
                }
            } else {
                extractFiles(f, to, accepter);
            }
        }
    }
}
