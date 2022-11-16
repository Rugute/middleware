package ke.or.karp.middleware.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "mapping")
    public class Mapping {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "dataelementname")
        private String dataelementname;

        @Column(name = "categoryoptioncomboname")
        private String categoryoptioncomboname;

        @Column(name = "DataelementUID")
        private String DataelementUID;

        @Column(name = "CategoryOptionComboUID")
        private String CategoryOptionComboUID;

        @Column(name = "DENMAPPER")
        private String DENMAPPER;

        @Column(name = "DATAELEMENTSPECIF")
        private String DATAELEMENTSPECIF;

        @Column(name = "CATEGORYCOMBOYEAR")
        private String CATEGORYCOMBOYEAR;

        @Column(name = "CATEGORYCOMBOGENDER")
        private String CATEGORYCOMBOGENDER;

        @Column(name = "CATEGORYCOMBOSPECIFIC")
        private String CATEGORYCOMBOSPECIFIC;

        @Column(name = "DATAELEMENTFINAL")
        private String DATAELEMENTFINAL;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDataelementname() {
            return dataelementname;
        }

        public void setDataelementname(String dataelementname) {
            this.dataelementname = dataelementname;
        }

        public String getCategoryoptioncomboname() {
            return categoryoptioncomboname;
        }

        public void setCategoryoptioncomboname(String categoryoptioncomboname) {
            this.categoryoptioncomboname = categoryoptioncomboname;
        }

        public String getDataelementUID() {
            return DataelementUID;
        }

        public void setDataelementUID(String dataelementUID) {
            DataelementUID = dataelementUID;
        }

        public String getCategoryOptionComboUID() {
            return CategoryOptionComboUID;
        }

        public void setCategoryOptionComboUID(String categoryOptionComboUID) {
            CategoryOptionComboUID = categoryOptionComboUID;
        }

        public String getDENMAPPER() {
            return DENMAPPER;
        }

        public void setDENMAPPER(String DENMAPPER) {
            this.DENMAPPER = DENMAPPER;
        }

        public String getDATAELEMENTSPECIF() {
            return DATAELEMENTSPECIF;
        }

        public void setDATAELEMENTSPECIF(String DATAELEMENTSPECIF) {
            this.DATAELEMENTSPECIF = DATAELEMENTSPECIF;
        }

        public String getCATEGORYCOMBOYEAR() {
            return CATEGORYCOMBOYEAR;
        }

        public void setCATEGORYCOMBOYEAR(String CATEGORYCOMBOYEAR) {
            this.CATEGORYCOMBOYEAR = CATEGORYCOMBOYEAR;
        }

        public String getCATEGORYCOMBOGENDER() {
            return CATEGORYCOMBOGENDER;
        }

        public void setCATEGORYCOMBOGENDER(String CATEGORYCOMBOGENDER) {
            this.CATEGORYCOMBOGENDER = CATEGORYCOMBOGENDER;
        }

        public String getCATEGORYCOMBOSPECIFIC() {
            return CATEGORYCOMBOSPECIFIC;
        }

        public void setCATEGORYCOMBOSPECIFIC(String CATEGORYCOMBOSPECIFIC) {
            this.CATEGORYCOMBOSPECIFIC = CATEGORYCOMBOSPECIFIC;
        }

        public String getDATAELEMENTFINAL() {
            return DATAELEMENTFINAL;
        }

        public void setDATAELEMENTFINAL(String DATAELEMENTFINAL) {
            this.DATAELEMENTFINAL = DATAELEMENTFINAL;
        }
    }


