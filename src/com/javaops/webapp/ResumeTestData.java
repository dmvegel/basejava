package com.javaops.webapp;

import com.javaops.webapp.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");
        fillSections(resume);

        System.out.println(resume.getFullName());
        System.out.println(resume.getContacts());
        System.out.println(SectionType.OBJECTIVE);
        System.out.println(resume.getSections().get(SectionType.OBJECTIVE));
        System.out.println(SectionType.PERSONAL);
        System.out.println(resume.getSections().get(SectionType.PERSONAL));
        System.out.println(SectionType.ACHIEVEMENT);
        ((ListSection) resume.getSections().get(SectionType.ACHIEVEMENT)).getTexts().forEach(System.out::println);
        System.out.println(SectionType.QUALIFICATIONS);
        ((ListSection) resume.getSections().get(SectionType.QUALIFICATIONS)).getTexts().forEach(System.out::println);
        System.out.println(SectionType.EXPERIENCE);
        ((CompanySection) resume.getSections().get(SectionType.EXPERIENCE)).getBlocks().forEach(System.out::println);
        System.out.println(SectionType.EDUCATION);
        ((CompanySection) resume.getSections().get(SectionType.EDUCATION)).getBlocks().forEach(System.out::println);
    }

    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        fillSections(resume);
        return resume;
    }

    public static void fillSections(Resume resume) {
        resume.getContacts().put(ContactType.PHONE_NUMBER, "+7(921) 855-0482");
        resume.getContacts().put(ContactType.SKYPE, "skype:grigory.kislin");
        resume.getContacts().put(ContactType.EMAIL, "gkislin@yandex.ru");

        resume.getContacts().put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.getContacts().put(ContactType.GITHUB, "https://github.com/gkislin");
        resume.getContacts().put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.getContacts().put(ContactType.HOMEPAGE, "http://gkislin.ru/");

        resume.getSections().put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.getSections().put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        resume.getSections().put(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(
                "Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, " +
                        "система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект " +
                        "для комплексных DIY смет",
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). " +
                        "Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением " +
                        "на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера. ",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для " +
                        "алгоритмического трейдинга.",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов " +
                        "и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа."
        )));

        resume.getSections().put(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy",
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts",
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), " +
                        "JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).",
                "Python: Django.",
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka",
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, " +
                        "JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.",
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix",
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer",
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования",
                "Родной русский, английский \"upper intermediate\""
        )));

        resume.getSections().put(SectionType.EXPERIENCE, new CompanySection(Arrays.asList(
                new CompanyBlock("Java Online Projects", "http://javaops.ru/",
                        List.of(new Period(
                                LocalDate.parse("2013-10-01"),
                                Period.FOR_NOW,
                                "Автор проекта.",
                                "Создание, организация и проведение Java онлайн проектов и стажировок."))),
                new CompanyBlock("Wrike", "https://www.wrike.com/",
                        List.of(new Period(
                                LocalDate.parse("2014-10-01"),
                                LocalDate.parse("2016-01-01"),
                                "Старший разработчик (backend)",
                                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, " +
                                        "Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                                        "авторизация по OAuth1, OAuth2, JWT SSO."))),
                new CompanyBlock("RIT Center", null,
                        List.of(new Period(
                                LocalDate.parse("2012-04-01"),
                                LocalDate.parse("2014-10-01"),
                                "Java архитектор",
                                "Организация процесса разработки системы ERP для разных окружений: релизная политика, " +
                                        "версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), " +
                                        "конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной " +
                                        "части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), " +
                                        "сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN " +
                                        "для online редактирование из браузера документов MS Office. Maven + plugin development, " +
                                        "Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, " +
                                        "Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"))),
                new CompanyBlock("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/",
                        List.of(new Period(
                                LocalDate.parse("2010-12-01"),
                                LocalDate.parse("2012-04-01"),
                                "Ведущий программист",
                                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, " +
                                        "GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения " +
                                        "для администрирования, мониторинга и анализа результатов в области алгоритмического " +
                                        "трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."))),
                new CompanyBlock("Yota", "https://www.yota.ru/",
                        List.of(new Period(
                                LocalDate.parse("2008-06-01"),
                                LocalDate.parse("2010-12-01"),
                                "Ведущий специалист",
                                "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, " +
                                        "v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация " +
                                        "администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента " +
                                        "(Python/ Jython, Django, ExtJS)"))),
                new CompanyBlock("Enkata", "http://enkata.com/",
                        List.of(new Period(
                                LocalDate.parse("2007-03-01"),
                                LocalDate.parse("2008-06-01"),
                                "Разработчик ПО",
                                "Разработка информационной модели, проектирование интерфейсов, реализация и отладка " +
                                        "ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."))),
                new CompanyBlock("Siemens AG", "https://www.siemens.com/ru/ru/home.html",
                        List.of(new Period(
                                LocalDate.parse("2005-01-01"),
                                LocalDate.parse("2007-02-01"),
                                "Разработчик ПО",
                                "Разработка информационной модели, проектирование интерфейсов, реализация и отладка " +
                                        "ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."))),
                new CompanyBlock("Alcatel", "http://www.alcatel.ru/",
                        List.of(new Period(
                                LocalDate.parse("1997-09-01"),
                                LocalDate.parse("2005-01-01"),
                                "Инженер по аппаратному и программному тестированию",
                                "Разработка информационной модели, проектирование интерфейсов, реализация и отладка " +
                                        "ПО на мобильной IN платформе Siemens @vantage (Java, Unix).")))

        )));

        resume.getSections().put(SectionType.EDUCATION, new CompanySection(Arrays.asList(
                new CompanyBlock("Coursera", "https://www.coursera.org/course/progfun",
                        List.of(new Period(
                                LocalDate.parse("2013-05-01"),
                                LocalDate.parse("2013-03-01"),
                                "'Functional Programming Principles in Scala' by Martin Odersky"))),
                new CompanyBlock("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                        List.of(new Period(
                                LocalDate.parse("2011-03-01"),
                                LocalDate.parse("2011-04-01"),
                                "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'"))),
                new CompanyBlock("Siemens AG", "http://www.siemens.ru/",
                        List.of(new Period(
                                LocalDate.parse("2005-01-01"),
                                LocalDate.parse("2005-04-01"),
                                "3 месяца обучения мобильным IN сетям (Берлин)"))),
                new CompanyBlock("Alcatel", "http://www.alcatel.ru/",
                        List.of(new Period(
                                LocalDate.parse("1997-09-01"),
                                LocalDate.parse("1998-03-01"),
                                "6 месяцев обучения цифровым телефонным сетям (Москва)"))),
                new CompanyBlock("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                        "http://www.ifmo.ru/",
                        Arrays.asList(new Period(
                                        LocalDate.parse("1993-09-01"),
                                        LocalDate.parse("1996-07-01"),
                                        "Аспирантура (программист С, С++)"),
                                new Period(
                                        LocalDate.parse("1987-09-01"),
                                        LocalDate.parse("1993-07-01"),
                                        "Инженер (программист Fortran, C)"))),
                new CompanyBlock("Заочная физико-техническая школа при МФТИ", "https://mipt.ru/",
                        List.of(new Period(
                                LocalDate.parse("1984-09-01"),
                                LocalDate.parse("1987-06-01"),
                                "Закончил с отличием")))
        )));
    }
}
