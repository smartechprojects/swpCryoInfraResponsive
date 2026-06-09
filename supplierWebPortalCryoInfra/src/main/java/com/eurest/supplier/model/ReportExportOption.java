package com.eurest.supplier.model;

import javax.persistence.*;

@Entity
@Table(name = "REPORT_EXPORT_OPTIONS")
public class ReportExportOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPORT_OPTION_ID")
    private Integer id;

    @Column(name = "XLS_REMOVE_EMPTY_SPACE", nullable = false)
    private boolean xlsRemoveEmptySpaceBetweenRows;

    @Column(name = "XLS_ONE_PAGE_PER_SHEET", nullable = false)
    private boolean xlsOnePagePerSheet;

    @Column(name = "XLS_AUTO_DETECT_CELL", nullable = false)
    private boolean xlsAutoDetectCellType;

    @Column(name = "XLS_WHITE_BACKGROUND", nullable = false)
    private boolean xlsWhitePageBackground;

    @Column(name = "HTML_REMOVE_EMPTY_SPACE", nullable = false)
    private boolean htmlRemoveEmptySpaceBetweenRows;

    @Column(name = "HTML_WHITE_BACKGROUND", nullable = false)
    private boolean htmlWhitePageBackground;

    @Column(name = "HTML_USE_IMAGES", nullable = false)
    private boolean htmlUsingImagesToAlign;

    @Column(name = "HTML_WRAP_BREAK", nullable = false)
    private boolean htmlWrapBreakWord;

    public ReportExportOption() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isXlsRemoveEmptySpaceBetweenRows() {
        return xlsRemoveEmptySpaceBetweenRows;
    }

    public void setXlsRemoveEmptySpaceBetweenRows(boolean xlsRemoveEmptySpaceBetweenRows) {
        this.xlsRemoveEmptySpaceBetweenRows = xlsRemoveEmptySpaceBetweenRows;
    }

    public boolean isXlsOnePagePerSheet() {
        return xlsOnePagePerSheet;
    }

    public void setXlsOnePagePerSheet(boolean xlsOnePagePerSheet) {
        this.xlsOnePagePerSheet = xlsOnePagePerSheet;
    }

    public boolean isXlsAutoDetectCellType() {
        return xlsAutoDetectCellType;
    }

    public void setXlsAutoDetectCellType(boolean xlsAutoDetectCellType) {
        this.xlsAutoDetectCellType = xlsAutoDetectCellType;
    }

    public boolean isXlsWhitePageBackground() {
        return xlsWhitePageBackground;
    }

    public void setXlsWhitePageBackground(boolean xlsWhitePageBackground) {
        this.xlsWhitePageBackground = xlsWhitePageBackground;
    }

    public boolean isHtmlRemoveEmptySpaceBetweenRows() {
        return htmlRemoveEmptySpaceBetweenRows;
    }

    public void setHtmlRemoveEmptySpaceBetweenRows(boolean htmlRemoveEmptySpaceBetweenRows) {
        this.htmlRemoveEmptySpaceBetweenRows = htmlRemoveEmptySpaceBetweenRows;
    }

    public boolean isHtmlWhitePageBackground() {
        return htmlWhitePageBackground;
    }

    public void setHtmlWhitePageBackground(boolean htmlWhitePageBackground) {
        this.htmlWhitePageBackground = htmlWhitePageBackground;
    }

    public boolean isHtmlUsingImagesToAlign() {
        return htmlUsingImagesToAlign;
    }

    public void setHtmlUsingImagesToAlign(boolean htmlUsingImagesToAlign) {
        this.htmlUsingImagesToAlign = htmlUsingImagesToAlign;
    }

    public boolean isHtmlWrapBreakWord() {
        return htmlWrapBreakWord;
    }

    public void setHtmlWrapBreakWord(boolean htmlWrapBreakWord) {
        this.htmlWrapBreakWord = htmlWrapBreakWord;
    }
}
