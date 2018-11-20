
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Parser {
	
	private Document dom = null;
	private ArrayList<Libro> libro = null;

	public Parser() {
		libro = new ArrayList<Libro>();
	}

	public void parseFicheroXml(String fichero) {
		// creamos una factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			// creamos un documentbuilder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parseamos el XML y obtenemos una representación DOM
			dom = db.parse(fichero);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public void parseDocument() {
		// obtenemos el elemento raíz
		Element docEle = dom.getDocumentElement();

		// obtenemos el nodelist de elementos
		NodeList nl = docEle.getElementsByTagName("libro");
			
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				
				// obtenemos un elemento de la lista (Libro)
				Element el = (Element) nl.item(i);
				

				// obtenemos un objeto libro
				Libro p = getLibro(el);

				// lo añadimos al array
				libro.add(p);
			}
		}
	}
	
	
	/**
	  * 
	  * Recuperar libro. 
	  * @param libro
	  * @return
	  */
	 private Libro getLibro(Element libro){
		// Llamamos al método para sacar varios valores de ahí:
	    String editor = getTextValue(libro,"editor"),
	    	   titulo = getTextValue(libro,"titulo");
	   // ArrayList<Autor> autores = getAutoresCambiado(libro, "autor");
	    //autoresF(libro, "autores");
	    ArrayList<Autor> autores = autores(libro, "autores");
	    
	    // Código anterior:
	    /*
	    // Pueden ser varios autores, con lo cual necesita otra operación:
	    NodeList autores = libro.getElementsByTagName("nombre");
	    // Está inicializado para evitar que salga "nullNombre".
	    String lista = "";
	    // Recorre los nodos dentro de otro nodo, "child".
	    for (int i = 0; i < autores.getLength(); i++) { 
	    	// Coge el autor actual:
	    	Element e = (Element) autores.item(i);
	    	// Es un string que coge el valor del nodo hijo.
	    	lista += "- " + e.getFirstChild().getNodeValue() + " -";        	
	    } // Al sólo haber 1 no hice método aparte.*/
	    
	    // Los ints:
	    int paginas = getIntValue(libro,"paginas"),
	    	anyo = Integer.parseInt(getAtributeValue(libro,"titulo"));  
	    // A año tenía que sacar el atributo.
	    
	    // Creamos un objeto libro con estos datos y lo devolvemos:
	    Libro lib = new Libro(titulo, autores, editor, paginas, anyo);
	    return lib; 
		 
		 }
	 
	 
	 
	 /**
	  * Coge los autores. Método anterior sin modificar de XML.
	  * 
	  */
	/* private ArrayList<String> getAutores(Element ele, String tagName) {
			ArrayList<String> autores = new ArrayList<String>();
			NodeList nl = ele.getElementsByTagName(tagName);
			
			if (nl != null && nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				NodeList nlNombres = el.getElementsByTagName("nombre");
				
				if (nlNombres != null && nlNombres.getLength() > 0) {
					for (int i=0; i<nlNombres.getLength(); i++) {
						Element eNom = (Element) nlNombres.item(i);
						autores.add(eNom.getFirstChild().getTextContent());
					}
				}
			}
			return autores;
	}*/
	
	 /**
	  *  Coge Apellidos.
	  */
	 private ArrayList<String> getApellidosElement(Element ele, String tagName) {
			NodeList nl = ele.getElementsByTagName(tagName);
			ArrayList<String> apellidos = new ArrayList<>();
			String autor = "";
			
			if (nl != null && nl.getLength() > 0) {
				Element el = (Element) nl.item(0);
				
				NodeList nlApellidos = el.getElementsByTagName("apellidos");
				
				if (nlApellidos != null && nlApellidos.getLength() > 0) {
					for (int i=0; i < nlApellidos.getLength(); i++) {
							Element eApe = (Element) nlApellidos.item(i);
							
							autor = eApe.getFirstChild().getTextContent();
							apellidos.add(autor);
						}
				}
			}
			return apellidos;			
	}
	
	 
	 // Recoge nombres. 
	 private ArrayList<String> getNommbreElement(Element ele, String tagName) {//tagName == <autores>
		
			NodeList nodeList = ele.getElementsByTagName(tagName);
			ArrayList<String> alNombres = new ArrayList<>();
			String sAutor = "";// para que no sale null  
			
			if (nodeList != null && nodeList.getLength() > 0) {// Comprobar que no esta vacio
				Element el = (Element) nodeList.item(0);// pilla la primera etique de autores
				
				NodeList nlNombre = el.getElementsByTagName("nombre");// Vamos a por la etiqueta nombre
				
				if (nlNombre != null && nlNombre.getLength() > 0) {// si hay algo en la etiqueta nombre que recorra todo lo que hay aen la etiqueta
					for (int i=0; i < nlNombre.getLength(); i++) {
							Element eNom = (Element) nlNombre.item(i);
							
							sAutor = eNom.getFirstChild().getTextContent();//obtenemos su valor
							alNombres.add(sAutor);// añadimos al array list
						}
				}
			}
			return alNombres;			
	}
	
	 /**
	  * Junta nombres y apellidos devolviendolos.
	  */
	 public ArrayList<Autor> autores(Element ele, String tagName){
		 ArrayList<Autor> alAutores = new ArrayList<>();
		 ArrayList<String> alNombres = new ArrayList<>();
		 ArrayList<String> alApellidos = new ArrayList<>();
		 Autor oAutor;
		 String nombre, apellidos;
		 
		 alNombres = getNommbreElement(ele, tagName);// coge el nombre de cada etiqueta y lo guarda en un ArrayList
		 alApellidos = getApellidosElement(ele, tagName);// coge el apellidos de cada etiqueta y lo guarda en un ArrayList
		 
		 for(int i = 0; i < alNombres.size(); i++) {//,size es el .length de array list
			 nombre = alNombres.get(i);//obtiene el nombre de esa posicion
			 apellidos = alApellidos.get(i);
			 
			 oAutor = new Autor(nombre, apellidos);//Creamos el objeto autor y lo almacenamos
			
			 alAutores.add(oAutor);//  le pasamos el objeto Autor y que lo aguarde en el array List 
		 }
	
		 return alAutores;
	 }

	  
	  
	  /**
	   *  Valor dentro de la respectiva etiqueta. 
	   */
	  private String getTextValue(Element ele, String tagName) {
	    String textVal = null;
	    NodeList nodeList = ele.getElementsByTagName(tagName);
	    
	    // Si el nodo tiene nombre o contenido:
	    if(nodeList != null && nodeList.getLength() > 0) {
	      Element element = (Element)nodeList.item(0);
	      textVal = element.getFirstChild().getNodeValue();
	    }  
	    
	    return textVal;
	  }
	  
	  
	  /**
	   *  Obtener atributo.
	   *  
	   */
	  private String getAtributeValue(Element ele, String tagName) {
	    String textVal = null;
	    NodeList nodeList = ele.getElementsByTagName(tagName);
	    
	    if(nodeList != null && nodeList.getLength() > 0) {
	      Element element = (Element)nodeList.item(0);
	      textVal = element.getAttribute("anyo");
	    }    
	    
	    return textVal;
	  }
	  
	  /**
	   *  Obtención del valor en formato de Integer para las páginas.
	   */
	  private int getIntValue(Element ele, String tagName) {  
	    return Integer.parseInt(getTextValue(ele,tagName));
	  }
	  
	
	  /**
	   * Print de libros
	   */
	  public void printLibro() {
		Iterator<Libro> it = libro.iterator();
		StringBuilder sb = new StringBuilder();
		
		while(it.hasNext()) {
		 Libro l = it.next();
		 // Llama al método toString() de libro para sacar el formato deseado:
	     sb.append(l.toString() + "\n\n");
	    }
		System.out.println(sb);;	
	  }


}