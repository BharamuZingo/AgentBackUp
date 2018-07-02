package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels.com on 04-11-2017.
 */

public class Documents {

    @SerializedName("DocumentId")
    private int DocumentId;
    @SerializedName("DocumentType")
    private String DocumentType;
    @SerializedName("DocumentNumber")
    private String DocumentNumber;

    public int getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(int documentId) {
        this.DocumentId = documentId;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        this.DocumentType = documentType;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.DocumentNumber = documentNumber;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        this.DocumentName = documentName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getReEnterDocumentNumber() {
        return ReEnterDocumentNumber;
    }

    public void setReEnterDocumentNumber(String reEnterDocumentNumber) {
        this.ReEnterDocumentNumber = reEnterDocumentNumber;
    }
    @SerializedName("DocumentName")
    private String DocumentName;
    @SerializedName("Status")
    private String Status;
    @SerializedName("ReEnterDocumentNumber")
    private String ReEnterDocumentNumber;


}
