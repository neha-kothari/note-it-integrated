package com.noteit.service;

import com.noteit.chapter.Chapter;
import com.noteit.notebook.Notebook;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class PDFService {

    @Value(("${noteit.notebook.path}"))
    private String notebookBasePath;

    public String merge(Notebook notebook) {

        System.out.println("notebookBasePath : " + notebookBasePath);
        String nbFolderPath = notebookBasePath + File.separator + notebook.getNotebookId();
        System.out.println("nbFolderPath : " + nbFolderPath);
        makeDirectory(nbFolderPath);
        try {
            List<String> extractedChapterLocations = extractChapters(nbFolderPath, notebook.getChapters());
            String indexPage = addCoverPage(notebook.getNotebookName(), notebook.getUserId().getName(), nbFolderPath);
            String fileLocation = mergePDFs(extractedChapterLocations, notebook.getNotebookId(), indexPage);
            return generate_page_numbers(fileLocation);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            deleteDirectory(nbFolderPath);
        }
        return null;

    }

    private void makeDirectory(String path) {

        File f1 = new File(path);
        //Creating a folder using mkdir() method
        boolean bool = f1.mkdir();
        if (bool){
            System.out.println("Folder is created successfully");
        } else {
            System.out.println("Error Found!");
        }
    }

    private void deleteDirectory(String filepath) {

        try {
            File file = new File(filepath);

            // call deleteDirectory method to delete directory recursively
            FileUtils.deleteDirectory(file);

            // delete folder
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not delete directory");
        }

    }

    public List<String> extractChapters(String nbFolderPath, Set<Chapter> chapterDetails) throws IOException {
        List<String> extractedChapterLocation = new ArrayList<>();
        int count = 0;
        for(Chapter chapter: chapterDetails) {
            String bookLocation = chapter.getBook().getFileLocation();
            count++;
            extractedChapterLocation.add(splitPdf(bookLocation, chapter.getStartPage(), chapter.getEndPage(), nbFolderPath, count));
        }
        return extractedChapterLocation;
    }

    private String splitPdf(String bookLocation, int fromPage, int toPage, String nbFolderPath, int count) throws IOException {

        // Loading PDF
        System.out.println("bookLocation == " + bookLocation);
        File pdfFile = new File(bookLocation);
        PDDocument document = PDDocument.load(pdfFile);

        if (document.getNumberOfPages() > toPage) {
            System.out.println(document.getDocumentInformation().getTitle());
            try {
                Splitter splitter = new Splitter();
                splitter.setStartPage(fromPage);
                splitter.setEndPage(toPage);
                splitter.setSplitAtPage(toPage);
                List<PDDocument> splittedChapters = splitter.split(document);
                String fileName = nbFolderPath + File.separator + count + ".pdf";
                for (PDDocument doc : splittedChapters) {
                    doc.save( fileName);
                    doc.close();
                }
                System.out.println("Save successful file : " + fileName);
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                document.close();
            }
        }
        return null;
    }

    public String addCoverPage(String customEBookName, String fullName, String orderFolderPath) throws IOException {
        // Create a Document object.
        PDDocument pdDocument = new PDDocument();

        // Create a Page object
        PDPage pdPage = new PDPage();
        // Add the page to the document and save the document to a desired file.
        pdDocument.addPage(pdPage);
        String destination = orderFolderPath+File.separator+"index.pdf";

        try {
            // Create a Content Stream
            PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, pdPage);

            float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(customEBookName) / 1000 * 40;
            float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 40;

            // Let's try a different font and size
            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset((pdPage.getMediaBox().getWidth() - titleWidth) / 2,
                    pdPage.getMediaBox().getHeight() - 30 - titleHeight);
            pdPageContentStream.setFont(PDType1Font.HELVETICA_BOLD, 40);
            pdPageContentStream.showText(customEBookName);
            pdPageContentStream.endText();

            titleWidth = PDType1Font.COURIER.getStringWidth(fullName) / 1000 * 18;
            pdPageContentStream.beginText();
            pdPageContentStream.newLineAtOffset((pdPage.getMediaBox().getWidth() - titleWidth) / 2, 50);
            pdPageContentStream.setFont(PDType1Font.COURIER, 18);
            pdPageContentStream.showText(fullName);
            pdPageContentStream.endText();

            // Once all the content is written, close the stream
            pdPageContentStream.close();
            pdDocument.save(destination);
            pdDocument.close();
            System.out.println("PDF saved to the location !!!");
            return destination;

        } catch (IOException ioe) {
            System.out.println("Error while saving pdf" + ioe.getMessage());
        } finally {
            pdDocument.close();
        }
        return null;
    }

    public String mergePDFs(List<String> chaptersPDFList, Long nbId, String indexPageLocation) throws IOException {

        //Create PDFMergerUtility class object
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        String destinationFileName = notebookBasePath + File.separator + "Notebook-" + nbId + ".pdf";
        //Setting the destination file path
        PDFmerger.setDestinationFileName(destinationFileName);

        File indexPage = new File(indexPageLocation);
        PDFmerger.addSource(indexPage);

        for(String chapterLocation: chaptersPDFList){
            File file = new File(chapterLocation);
            //adding the source files
            PDFmerger.addSource(file);
        }

        //Merging the documents
        PDFmerger.mergeDocuments(null);
        System.out.println("PDF Documents merged to a single file successfully");
        return destinationFileName;
    }

    public String generate_page_numbers(String file_name) {
        File load_file = new File(file_name);
        PDDocument doc;
        try {

            doc = PDDocument.load(load_file);
            int page_number = 1;
            for (PDPage page : doc.getPages()) {
                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, false);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ITALIC, 10);
                contentStream.setStrokingColor(Color.BLACK);
                PDRectangle pageSize = page.getCropBox();
                float x = pageSize.getLowerLeftX();
                float y = pageSize.getLowerLeftY();
                contentStream.newLineAtOffset(x + pageSize.getWidth() - 60, y + 20);
                contentStream.showText(Integer.toString(page_number));
                contentStream.endText();
                contentStream.close();
                ++page_number;
            }

            doc.save(load_file);
            doc.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return file_name;
    }

    public int getTotalNumberOfPages(String bookLocation) throws IOException {
        File pdfFile = new File(bookLocation);
        PDDocument document = PDDocument.load(pdfFile);

        return document.getNumberOfPages();
    }
}
