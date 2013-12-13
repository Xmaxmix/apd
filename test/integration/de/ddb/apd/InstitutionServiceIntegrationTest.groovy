package de.ddb.apd;

import static org.junit.Assert.*
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import org.junit.Test

class InstitutionServiceIntegrationTest extends GroovyTestCase {

    def institutionService

    def itemService

    /**
     * Test for getting all root institutions.
     */
    @Test public void findAllTest() {
        def allInstitutions = institutionService.findAll()

        println "allInstitutions: " + allInstitutions.size()
        assert allInstitutions.size() > 0
    }

    /**
     * Test for getTotal() on the root institutions
     */
    @Test public void getTotalTest() {
        def allInstitutions = institutionService.findAll()

        def total = institutionService.getTotal(allInstitutions)

        assert total > allInstitutions.size()
    }

    /**
     * Test for getting the alphabetical index of the root institutions.
     * Checks that each institution has been set in the index.
     */
    @Test public void findAllAlphabeticalTest() {
        def allInstitutions = institutionService.findAll()
        def total = institutionService.getTotal(allInstitutions)
        def totalAlphabetical = 0

        def findAllAlphabetical = institutionService.findAllAlphabetical()

        findAllAlphabetical.data.each { index, list ->
            totalAlphabetical += institutionService.getTotal(list)
        }

        //The total number of institutions get by findAll must be equal to that number of institutions stored in the alphabetical index
        assert total == totalAlphabetical
    }

    /**
     * Test for retrieving all archive institutions for the structure
     */
    @Test public void searchArchivesForStructureTest() {
        def archives = institutionService.searchArchivesForStructure()

        println "total archives: " + archives.count

        assert archives.count > 0
    }

    /**
     * Test for retrieving all archive institutions that holds items for a given query
     */
    @Test public void searchArchivesWithQueryTest() {
        def archives = institutionService.searchArchives("Goethe")

        assert archives.count > 0
    }

    /**
     * Test for getting the alphabetical index of the root institutions.
     * Checks that each institution has been set in the index.
     */
    @Test public void getInstitutionState() {
        def timeStart = new Date()

        def state = institutionService.getInstitutionState("UP63QLFLIUQHB2KXMOHBLHGVXBBLICRZ")

        def timeStop = new Date()

        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        println "duration: " + duration

        assert state == 'Bayern'
    }

    /**
     * Test for getting the alphabetical index of the root institutions.
     * Checks that each institution has been set in the index.
     */
    @Test public void getAllInstitutionWithState() {
        //        def timeStart = new Date()
        //
        //        def allInstitutions = institutionService.findAll()
        //
        //        allInstitutions.each {
        //            def institution = institutionService.findInstitutionViewById(it.id)
        //        }
        //
        //        // Some code you want to time
        //        def timeStop = new Date()
        //
        //        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        //        println "duration: " + duration
    }

    /**
     * Test to retrieve the parent of a given institution
     */
    @Test public void getInstitutionParentTest() {
        def parent = institutionService.getInstitutionParent("MXKPZWYNEUUZCDLAU4U5LCJJSUE2CUAH")

        assert parent.parent[0] == "WEVZTYMZKPRKULXN22O4BGBS3N3D3ZOR"
    }
}
