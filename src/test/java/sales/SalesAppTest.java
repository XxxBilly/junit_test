package sales;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SalesAppTest {

    private SalesReportDao salesReportDao;
    private EcmService ecmService;
    private final String SALES_ACTIVITY = "SalesActivity";


    @Before
    public void setUp() {
        salesReportDao = mock(SalesReportDao.class);
        ecmService = mock(EcmService.class);
    }

    @Test
    public void testGenerateReport() {
        SalesApp mSalesApp = spy(new SalesApp());
        SalesActivityReport report = new SalesActivityReport();
        List<SalesReportData> reportDataList = new ArrayList<>();

        doReturn(false).when(mSalesApp).isNotEffectiveSales(any());
        doReturn(report).when(mSalesApp).generateReport(any(), any());
        doReturn(salesReportDao).when(mSalesApp).getSalesReportDao();
        doReturn(ecmService).when(mSalesApp).getEcmService();
        doReturn(reportDataList).when(salesReportDao).getReportData(any());

        mSalesApp.generateSalesActivityReport("DUMMY", 1000, false, false);

        verify(ecmService).uploadDocument(any());
    }

    @Test
    public void testGetHeadersByNatTrade() {
        SalesApp mSalesApp = spy(new SalesApp());

        List<String> headers = mSalesApp.getHeadersByNatTrade(false);

        assertEquals("Local Time", headers.get(headers.size() - 1));
    }

    @Test
    public void testIsNotEffectiveSales() {
        SalesApp mSalesApp = spy(new SalesApp());
        Sales sales = spy(new Sales());

        doReturn(new Date(2019, 2, 14)).when(sales).getEffectiveFrom();
        doReturn(new Date(2020, 2, 14)).when(sales).getEffectiveTo();

        boolean result = mSalesApp.isNotEffectiveSales(sales);

        assertTrue(result);
    }

    @Test
    public void testFilterReportDataList() {
        SalesApp mSalesApp = spy(new SalesApp());

        List<SalesReportData> result = mSalesApp.filterReportDataList(true, getSalesReportData());

        assertEquals(2, result.size());
    }

    @Test
    public void testFilterRedundantData() {
        SalesApp mSalesApp = spy(new SalesApp());

        List<SalesReportData> result = mSalesApp.filterRedundantData(1, getSalesReportData());

        assertEquals(1, result.size());
    }


    private List<SalesReportData> getSalesReportData() {
        List<SalesReportData> data = new ArrayList<>();

        SalesReportData salesReportData1 = mock(SalesReportData.class);
        doReturn(SALES_ACTIVITY).when(salesReportData1).getType();
        doReturn(false).when(salesReportData1).isConfidential();

        SalesReportData salesReportData2 = mock(SalesReportData.class);
        doReturn(SALES_ACTIVITY).when(salesReportData2).getType();
        doReturn(true).when(salesReportData2).isConfidential();

        SalesReportData salesReportData3 = mock(SalesReportData.class);
        doReturn("").when(salesReportData3).getType();
        doReturn(false).when(salesReportData3).isConfidential();

        data.add(salesReportData1);
        data.add(salesReportData2);
        data.add(salesReportData3);

        return data;
    }
}
