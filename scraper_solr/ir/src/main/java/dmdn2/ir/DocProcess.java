package dmdn2.ir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocProcess {
	public String professore;
	public String materia;
	public String anno;
	public String link;
	public String tipologia;
	public String folder;
	
	public String doc;
	public String link_doc;
	
	public List<String> page_list = new ArrayList<String>();


	
	public DocProcess(Scraper sca, String link_doc, String doc) {
		super();
		this.professore = sca.professore;
		this.materia = sca.materia;
		this.anno = sca.anno;
		this.tipologia = sca.tipologia;
		this.folder = sca.folder;
		this.link=sca.link;
		
		this.doc = doc;
		this.link_doc = link_doc;
	}
	
	
	public void processa_doc() throws IOException{
		System.out.println(this.doc);
		if (this.doc.substring(this.doc.length() - 3).equals("pdf")) {
		  
			File pdfFile = new File("src/main/resources/cose/"+this.folder+ this.doc);
			PDDocument doc = PDDocument.load(pdfFile);
			int num_pag=doc.getNumberOfPages();
			
			for (int i=0; i<=num_pag; i++) {
				PDFTextStripper pdfStripper= new PDFTextStripper();
				pdfStripper.setStartPage(i);
		        pdfStripper.setEndPage(i);
		        String parsedText = pdfStripper.getText(doc);
		        parsedText=Tokenaizer.clean(parsedText);
		        this.page_list.add(parsedText);
		        
			}
			
			
		}

		/* typefile check .ppt controllando gli ultimi 4 caratteri .ppt */

		if (this.doc.substring(this.doc.length() - 3).equals("ppt")) {
			HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl("src/main/resources/cose/"+this.folder+ this.doc));

			List<HSLFSlide> slides = ppt.getSlides();
			
			for ( HSLFSlide slide : slides) {
				 for (HSLFShape sh : slide.getShapes()) {


					 if (sh instanceof HSLFTextShape) {
						 	HSLFTextShape shape = (HSLFTextShape) sh;
						 	String parsedText = shape.getText();
						 	parsedText=Tokenaizer.clean(parsedText);
						 	this.page_list.add(parsedText);
				            // work with a shape that can hold text
				        }
					 
				 }
			}

			  
		}

		/* typefile check .doc controllando gli ultimi 4 caratteri .doc */

		if (this.doc.substring(this.doc.length() - 3).equals("doc")) {
			 try {
				   FileInputStream fis = new FileInputStream("src/main/resources/cose/"+this.folder+ this.doc);
				   HWPFDocument xdoc = new HWPFDocument(fis);
				   WordExtractor we = new WordExtractor(xdoc);
				   
				   String parsedText=we.getText();
				   parsedText=Tokenaizer.clean(parsedText);
				   this.page_list.add(parsedText);

				} catch(Exception ex) {
				    ex.printStackTrace();
				}

		}

		/* typefile check .pptx controllando gli ultimi 4 caratteri .pptx */

		
		if (this.doc.substring(this.doc.length() - 4).equals("pptx")) {
			 XMLSlideShow ppt = new XMLSlideShow(new FileInputStream("src/main/resources/cose/"+this.folder+ this.doc));
			List<XSLFSlide> slides = ppt.getSlides();
			for ( XSLFSlide slide : slides) {
				 for (XSLFShape sh : slide.getShapes()) {


					 if (sh instanceof XSLFTextShape) {
				            XSLFTextShape shape = (XSLFTextShape) sh;
							   String parsedText=shape.getText();
							   parsedText=Tokenaizer.clean(parsedText);
							   this.page_list.add(parsedText);
				            // work with a shape that can hold text
				        }
					 
				 }
			}

		}

	
		/* typefile check .docx controllando gli ultimi 4 caratteri .docx */

		if (this.doc.substring(this.doc.length() - 4).equals("docx")) {
			 try {
				   FileInputStream fis = new FileInputStream("src/main/resources/cose/"+this.folder+ this.doc);
				   XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
				   XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
				   String parsedText=extractor.getText();
				   parsedText=Tokenaizer.clean(parsedText);
				   this.page_list.add(parsedText);
				} catch(Exception ex) {
				    ex.printStackTrace();
				}
		}
		
		
		
		
	}
	

	
	
	

}