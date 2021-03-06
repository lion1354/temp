package cn.forp.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

//http://www.cnblogs.com/liuk/p/5829389.html
public class Test
{
	public static String beanToXml(Object obj, Class<?> load) throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(load);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
		StringWriter writer = new StringWriter();
		marshaller.marshal(obj, writer);
		return writer.toString();
	}

	public static Object xmlToBean(String xmlPath, Class<?> load) throws JAXBException, IOException
	{
		JAXBContext context = JAXBContext.newInstance(load);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Object object = unmarshaller.unmarshal(new File(xmlPath));
		return object;
	}

	public static void main(String[] args) throws Exception
	{
//		RuleSet ruleSet1 = new RuleSet("mandatory", "prodAltNum|prodCdeAlt|delay", null);
//		RuleSet ruleSet2 = new RuleSet("mandatory", "prodAltNum|prodCdeAlt|delay", "market");
//		List<RuleSet> ruleSets = new ArrayList<RuleSet>();
//		ruleSets.add(ruleSet1);
//		ruleSets.add(ruleSet2);
//		Entity entity1 = new Entity("locale|delay|tradeDay", ruleSets, "testserciceName");
//		Entity entity2 = new Entity("locale|delay|tradeDay", ruleSets, "testserciceNamewer");
//		ServiceEntity ServiceEntity1 = new ServiceEntity("QUOTE_DETAIL", entity1);
//		ServiceEntity ServiceEntity2 = new ServiceEntity("QUOTE_LIST", entity2);
//		List<ServiceEntity> ServiceEntity = new ArrayList<ServiceEntity>();
//		ServiceEntity.add(ServiceEntity1);
//		ServiceEntity.add(ServiceEntity2);
//		Service service = new Service(ServiceEntity);
//		System.out.println(beanToXml(service, Service.class));
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String fromDate = "2016-05-01 12:00";  
		String toDate = "2016-05-01 14:00";  
		long from = simpleFormat.parse(fromDate).getTime();  
		System.out.println(from);
		System.out.println(simpleFormat.parse("2016-05-01 00:00").getTime());
		long to = simpleFormat.parse(toDate).getTime();  
		int hours = (int) ((to - from)/(1000 * 60 * 60));  
		System.out.println(hours);
	}

}
