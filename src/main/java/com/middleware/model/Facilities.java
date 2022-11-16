package com.middleware.model;

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
    @Table(name = "facilities")
    public class Facilities {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "OrgUnitName")
        private String OrgUnitName;

        @Column(name = "facilityname")
        private String facilityname;

        @Column(name = "mflcode")
        private double mflcode;

        @Column(name = "OrgUnitUID")
        private String OrgUnitUID;

        @Column(name = "Partner")
        private String Partner;

        @Column(name = "ftype")
        private String ftype;

        @Column(name = "county")
        private String county;

        @Column(name = "subcounty")
        private String subcounty;

        @Column(name = "region")
        private String region;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrgUnitName() {
            return OrgUnitName;
        }

        public void setOrgUnitName(String orgUnitName) {
            OrgUnitName = orgUnitName;
        }

        public double getMflcode() {
            return mflcode;
        }

        public void setMflcode(double mflcode) {
            this.mflcode = mflcode;
        }

        public String getOrgUnitUID() {
            return OrgUnitUID;
        }

        public void setOrgUnitUID(String orgUnitUID) {
            OrgUnitUID = orgUnitUID;
        }

        public String getPartner() {
            return Partner;
        }

        public void setPartner(String partner) {
            Partner = partner;
        }

        public String getFacilityname() {
            return facilityname;
        }

        public void setFacilityname(String facilityname) {
            this.facilityname = facilityname;
        }

        public String getFtype() {
            return ftype;
        }

        public void setFtype(String ftype) {
            this.ftype = ftype;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getSubcounty() {
            return subcounty;
        }

        public void setSubcounty(String subcounty) {
            this.subcounty = subcounty;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

