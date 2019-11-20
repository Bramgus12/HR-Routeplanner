package com.bramgussekloo.projects.dataclasses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for XLSXReader")
public class XlsxReader {

    @ApiModelProperty(notes = "Number of the row", required = true)
    private int rowNum;

    @ApiModelProperty(notes = "Title of each column", required = true)
    private String columnTitle;

    @ApiModelProperty(notes = "Values in each row", required = true)
    private String content;

    public XlsxReader(int rowNum, String title, String content){
        this.rowNum = rowNum;
        this.columnTitle = title;
        this.content = content;
    }

    // Empty object for initial run
    public XlsxReader(){
    }

    public int getId() {
        return rowNum;
    }

    public void setId(int rowNum){
        this.rowNum = rowNum;
    }

    public String getContent(){
        return content;
    }

    public <T> void setContent(T content){
        this.content = content.toString();
    }

    public String getTitle() {
        return columnTitle;
    }

    public <T> void setTitle(T title) {
        this.columnTitle = title.toString();
    }
}
