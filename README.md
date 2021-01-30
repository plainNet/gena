# gena
Java tool for generating server resources as c-style arrays

Hello, I'm **Gena**!

My function is recycling your web project files into C-style arrays placed into FLASH (.text) memory. The result will be placed in <b>HttpHostDefaultResources.cpp</b>.

My command line arguments are listed below:
```
- web_folder:           defines location of folder with web 
                        project files (see example below);
	
- out:                  defines ABSOLUTE path for folder(s) where 
                        result file(s) will be placed (see example below);
	
- folder_ignore:        defines list of ignored folders inside 
                        "web_folder" (see example below);
	
- file_ignore:          defines list of ignored files
                        inside "web_folder" (see example below);
	
- folder_ignore_src:    defines RELATIVE path to list-file with 
                        enumeration ignored folders inside 
                        "web_folder" (see example below);
	
- file_ignore_src:      defines RELATIVE path to list-file with 
                        enumeration of ignored files inside
                        "web_folder" (see example below).
```
<b>EXAMPLES CONDITIONS</b>

1) We have embedded firmware project. Project uses plainNet and it's HttpHost unit. Option **PLAINNET_USE_DEFAULT_HTTP_RESOURCES = 1**, so HttpHostDefaultResources unit is used.
2) Embedded project sources are placed in **/opt/hardware/stm32/projects/em-proj** folder. Sources of plainNet are placed in plainNet/src folder inside project folder and includes in plainNet/inc.
```
/opt/hardware/stm32/projects/em-proj
                                |
                                *--> ...
                                |
                                *--> plainNet *--> inc *--> HttpHostDefaultResources.h
                                              |	       |
                                              |	       *--> ...
                                              |
                                              *--> src *--> HttpHostDefaultResources.cpp (!!!)
                                                       |
                                                       *--> ...
```
1) Web project of our HttpServer resources is placed in **/opt/hardware/stm32/projects/em-proj-web**. Inside this folder we have common web project folders structure

```
/opt/hardware/stm32/projects/em-proj-web
				|
				*--> .git
				|
				*--> README.md
				|
				*--> index.html
				|
				*--> js (folder) *-->script_1.js
				|                |
				|                *-->script_2.js
				|
				*--> css (folder) *--> styles.css
				|
				*--> less (folder) *--> header.less
                                                   |
                                                   *--> fonts.less
                                                   |
                                                   *--> styles.less
```

1) Gena is used to map all allowded resources in **/opt/hardware/stm32/projects/em-proj-web**
   into **HttpHostDefaultResources.cpp** as bytes arrays placed into FLASH (.text) memory.
	
2) Folder **/opt/hardware/stm32/projects/em-proj-web** contains files and folders that must be removed from HttpServer unit resources on **HttpHostDefaultResources.cpp** generate.

<b>EXAMPLES</b>

1) Using only comand line parameters:

```
   java -jar /path-to-gena/Gena.jar web_folder="/opt/hardware/stm32/projects/em-proj-web" out="/opt/hardware/stm32/projects/em-proj/plainNet/src" folder_ignore=less file_igonre=*.git,*.md
```
2) Using external ignore files

```
   java -jar /path-to-gena/Gena.jar web_folder="/opt/hardware/stm32/projects/em-proj-web" out="/opt/hardware/stm32/projects/em-proj/plainNet/src" folder_ignore_src=dir_igonre.ini file_ignore_src=file_igonre.ini
```
```
	**** dir_igonre.ini ****
	*                      *
	*	less           *
	*		       *
	************************

	**** file_igonre.ini ****
	*                       *
	*	*.git           *
	*	*.md		*
	*			*
	*************************
```	
In all examples Gena will ignore folder named "less" and all files in all directories with extensions *.git and *.md. C-style arrays will be generated for:

	- index.html (URI: / and /index.html);
	- script_1.js (URI: /js/script_1.js);
	- script_2.js (URI: /js/script_2.js);
	- styles.css (URI: /css/styles.css);
