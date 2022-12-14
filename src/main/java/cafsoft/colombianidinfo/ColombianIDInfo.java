package cafsoft.colombianidinfo;

import java.io.Serializable;

public final class ColombianIDInfo
        implements Serializable {

    private String documentTypeCode = "";
    private DocumentType documentType = DocumentType.OTHER;
    private int documentNumber = 0;
    private String bloodType = "";
    private String dateOfBirth = "";
    private String gender = "";
    private String firstFamilyName = "";
    private String secondFamilyName = "";
    private String firstName = "";
    private String otherNames = "";

    private static String clean(String aPDF417Data) {

        return aPDF417Data.replaceAll("\0", " ");
    }

    private static String extract(String text, int iniPos, int size) {

        return text.substring(iniPos, iniPos + size);
    }

    public static ColombianIDInfo decode(String aPDF417Data)
            throws Exception {

        String documentTypeCode = "";
        DocumentType documentType = DocumentType.OTHER;
        String documentNumber = "";
        String firstFamilyName = "";
        String secondFamilyName = "";
        String firstName = "";
        String otherNames = "";
        String gender = "";
        String dateOfBirth = "";
        String bloodType = "";

        String data = "";
        ColombianIDInfo info = null;

        if (aPDF417Data.length() != 531 && aPDF417Data.length() != 532) {
            throw new Exception("Error: invalid identification document information");
        }

        documentTypeCode = extract(aPDF417Data, 0, 2);
        if (documentTypeCode.equals("02") ||documentTypeCode.equals("03")) {
            documentType = DocumentType.CITIZENSHIP_CARD;
        } else if (documentTypeCode.equals("I3")) {
            documentType = DocumentType.IDENTITY_CARD;
        } else
            throw new Exception("Error: invalid identification document type");

        switch (documentType){
            case CITIZENSHIP_CARD:
                data = clean(extract(aPDF417Data, 0, 169));
                // Extract document number
                documentNumber = extract(data, 48, 10).trim();
                // Extract first family name
                firstFamilyName = extract(data, 58, 23).trim();
                // Extract second family name
                secondFamilyName = extract(data, 81, 23).trim();
                // Extract firstname
                firstName = extract(data, 104, 23).trim();
                // Extract other names
                otherNames = extract(data, 127, 23).trim();
                // Extract gender
                gender = extract(data, 151, 1).trim();
                // Extract date of birth (format YYYYMMDD)
                dateOfBirth = extract(data, 152, 8).trim();
                // Extract blood type (the ABO and Rh systems)
                bloodType = extract(data, 166, 3).trim();
                break;

            case IDENTITY_CARD:
                data = clean(extract(aPDF417Data, 0, 170));
                // Extract document number
                documentNumber = extract(data, 48, 10).trim();
                // Extract first family name
                firstFamilyName = extract(data, 59, 23).trim();
                // Extract second family name
                secondFamilyName = extract(data, 82, 23).trim();
                // Extract second family name
                firstName = extract(data, 105, 23).trim();
                // Extract other names
                otherNames = extract(data, 128, 23).trim();
                // Extract gender
                gender = extract(data, 152, 1).trim();
                // Extract date of birth (format YYYYMMDD)
                dateOfBirth = extract(data, 153, 8).trim();
                // Extract blood type (the ABO and Rh systems)
                bloodType = extract(data, 167, 3).trim();
                break;
        }

        info = new ColombianIDInfo();
        info.setDocumentTypeCode(documentTypeCode);
        info.setDocumentNumber(Integer.parseInt(documentNumber));
        info.setFirstFamilyName(firstFamilyName);
        info.setSecondFamilyName(secondFamilyName);
        info.setFirstName(firstName);
        info.setOtherNames(otherNames);
        info.setGender(gender);
        info.setDateOfBirth(dateOfBirth);

        String bloodGroup = "";
        char rh = '?';
        if (bloodType.length() == 3) {
            bloodGroup = extract(bloodType, 0, 2).trim();
            rh = bloodType.charAt(2);
        }else if (bloodType.length() == 2) {
            bloodGroup = extract(bloodType, 0, 1).trim();
            rh = bloodType.charAt(1);
        } else
            throw new Exception();

        if (!(bloodGroup.equals("A") || bloodGroup.equals("B") || bloodGroup.equals("AB")|| bloodGroup.equals("O")))
                throw new Exception("Error: Invalid ABO value");

        if (!(rh == '+' || rh == '-'))
            throw new Exception("Error: Invalid rh value");

        info.setBloodType(bloodType);

        return info;
    }

    public String getFamilyName() {

        return (getFirstFamilyName() + " " + getSecondFamilyName()).trim();
    }

    public String getName() {

        return (getFirstName() + " " + getOtherNames()).trim();
    }

    public String getFullname() {

        return getFamilyName() + " " + getName();
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstFamilyName() {
        return firstFamilyName;
    }

    public void setFirstFamilyName(String firstFamilyName) {
        this.firstFamilyName = firstFamilyName;
    }

    public String getSecondFamilyName() {
        return secondFamilyName;
    }

    public void setSecondFamilyName(String secondFamilyName) {
        this.secondFamilyName = secondFamilyName;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        if (documentTypeCode.equals("02") || documentTypeCode.equals("03"))
            documentType = DocumentType.CITIZENSHIP_CARD;
        else if (documentTypeCode.equals("I3"))
            documentType = DocumentType.IDENTITY_CARD;
        else
            documentType = DocumentType.OTHER;

        this.documentTypeCode = documentTypeCode;
    }

    public enum DocumentType {
        IDENTITY_CARD,
        CITIZENSHIP_CARD,
        OTHER
    }
}