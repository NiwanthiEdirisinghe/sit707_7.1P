package sit707_week7;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {

	@Test
	public void testStudentIdentity() {
		String studentId = "223558537";
		Assert.assertNotNull("Student ID is not null", studentId);
	}

	@Test
	public void testStudentName() {
		String studentName = "Niwanthi";
		Assert.assertNotNull("Student name is not null", studentName);
	}
	
	@Test
	public void testReadTemperatureNegative() {
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();

        Assert.assertTrue("Temperature should be negative", temperature < 0);
	}
	
	@Test
	public void testReadTemperatureZero() {
		TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();

        Assert.assertEquals("Temperature should be zero", 0.0, temperature, 0.01);
	}
	
	@Test
	public void testReadTemperatureNormal() {
		 TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
	        when(temperatureSensor.readTemperatureValue()).thenReturn(36.5);

	        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
	        double temperature = monitor.readTemperature();

	        Assert.assertTrue("Temperature should be normal", temperature >= 35 && temperature <= 37);
	}

	@Test
	public void testReadTemperatureAbnormallyHigh() {
		TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(40.0);

        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = monitor.readTemperature();

        Assert.assertTrue("Temperature should be abnormally high", temperature > 37);
    }
	

	/*
	 * CREDIT or above level students, Remove comments. 
	 */
	@Test
	public void testReportTemperatureReadingToCloud() {
		// Mock reportTemperatureReadingToCloud() calls cloudService.sendTemperatureToCloud()
		CloudService cloudService = Mockito.mock(CloudService.class);
        TemperatureReading temperatureReading = new TemperatureReading();
        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, null);
        
        monitor.reportTemperatureReadingToCloud(temperatureReading);

        verify(cloudService, times(1)).sendTemperatureToCloud(temperatureReading);
	}
	
	
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
	@Test
	public void testInquireBodyStatusNormalNotification() {
		CloudService cloudService = Mockito.mock(CloudService.class);
        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("NORMAL");

        // Mock notificationSender to verify if sendEmailNotification(Customer) is called
        NotificationSender notificationSender = Mockito.mock(NotificationSender.class);
        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);

        monitor.inquireBodyStatus();

        verify(notificationSender, times(1)).sendEmailNotification(any(Customer.class), anyString());
	}
	
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
	@Test
	public void testInquireBodyStatusAbnormalNotification() {
		CloudService cloudService = Mockito.mock(CloudService.class);
        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("ABNORMAL");

        // Mock notificationSender to verify if sendEmailNotification(FamilyDoctor) is called
        NotificationSender notificationSender = Mockito.mock(NotificationSender.class);
        BodyTemperatureMonitor monitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);
        
        monitor.inquireBodyStatus();

        verify(notificationSender, times(1)).sendEmailNotification(any(FamilyDoctor.class), anyString());
	}
}
