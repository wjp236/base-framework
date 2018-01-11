package com.platform.base.frramework.trunk.util.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by mylover on 5/23/16.
 */
public class ObjectUtil {
    private JAXBContext jaxbContext;

    public ObjectUtil(Class... types) {
        try {
            this.jaxbContext = JAXBContext.newInstance(types);
        } catch (JAXBException var3) {
            throw new RuntimeException(var3);
        }
    }

    public String toXml(Object root) {
        try {
            StringWriter e = new StringWriter();
            this.createMarshaller("UTF-8", (String)null, Boolean.valueOf(true)).marshal(root, e);
            return e.toString();
        } catch (JAXBException var3) {
            throw new RuntimeException(var3);
        }
    }

    public String toXml(Object root, String encoding) {
        try {
            StringWriter e = new StringWriter();
            this.createMarshaller(encoding, (String)null, Boolean.valueOf(false)).marshal(root, e);
            return e.toString();
        } catch (JAXBException var4) {
            throw new RuntimeException(var4);
        }
    }

    public String toXml(Object root, String encoding, String declaration) {
        try {
            StringWriter e = new StringWriter();
            e.append(declaration);
            this.createMarshaller(encoding, declaration, Boolean.valueOf(false)).marshal(root, e);
            return e.toString();
        } catch (JAXBException var5) {
            throw new RuntimeException(var5);
        }
    }

    public Marshaller createMarshaller(String encoding, String declaration, Boolean formatted) {
        try {
            Marshaller e = this.jaxbContext.createMarshaller();
            if(formatted.booleanValue()) {
                e.setProperty("jaxb.formatted.output", Boolean.TRUE);
            }

            if(encoding != null && !"".equals(encoding)) {
                e.setProperty("jaxb.encoding", encoding);
            }

            if(declaration != null && !"".equals(declaration)) {
                e.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            }

            return e;
        } catch (JAXBException var5) {
            throw new RuntimeException(var5);
        }
    }


    public <T> T fromXml(String xml) {
        try {
            StringReader e = new StringReader(xml);
            return (T) this.createUnmarshaller().unmarshal(e);
        } catch (JAXBException var3) {
            throw new RuntimeException(var3);
        }
    }

    public Unmarshaller createUnmarshaller() {
        try {
            return this.jaxbContext.createUnmarshaller();
        } catch (JAXBException var2) {
            throw new RuntimeException(var2);
        }
    }
}
