
public class Parser_Libro {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parser parser=new Parser();
		parser.parseFicheroXml("biblioteca.xml");
		parser.parseDocument();
		parser.printLibro();
	}

}
