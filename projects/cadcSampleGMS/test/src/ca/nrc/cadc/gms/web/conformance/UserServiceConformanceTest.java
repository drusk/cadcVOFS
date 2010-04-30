/**
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2010.                            (c) 2010.
 *  Government of Canada                 Gouvernement du Canada
 *  National Research Council            Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 *  All rights reserved                  Tous droits réservés
 *
 *  NRC disclaims any warranties,        Le CNRC dénie toute garantie
 *  expressed, implied, or               énoncée, implicite ou légale,
 *  statutory, of any kind with          de quelque nature que ce
 *  respect to the software,             soit, concernant le logiciel,
 *  including without limitation         y compris sans restriction
 *  any warranty of merchantability      toute garantie de valeur
 *  or fitness for a particular          marchande ou de pertinence
 *  purpose. NRC shall not be            pour un usage particulier.
 *  liable in any event for any          Le CNRC ne pourra en aucun cas
 *  damages, whether direct or           être tenu responsable de tout
 *  indirect, special or general,        dommage, direct ou indirect,
 *  consequential or incidental,         particulier ou général,
 *  arising from the use of the          accessoire ou fortuit, résultant
 *  software.  Neither the name          de l'utilisation du logiciel. Ni
 *  of the National Research             le nom du Conseil National de
 *  Council of Canada nor the            Recherches du Canada ni les noms
 *  names of its contributors may        de ses  participants ne peuvent
 *  be used to endorse or promote        être utilisés pour approuver ou
 *  products derived from this           promouvoir les produits dérivés
 *  software without specific prior      de ce logiciel sans autorisation
 *  written permission.                  préalable et particulière
 *                                       par écrit.
 *
 *  This file is part of the             Ce fichier fait partie du projet
 *  OpenCADC project.                    OpenCADC.
 *
 *  OpenCADC is free software:           OpenCADC est un logiciel libre ;
 *  you can redistribute it and/or       vous pouvez le redistribuer ou le
 *  modify it under the terms of         modifier suivant les termes de
 *  the GNU Affero General Public        la “GNU Affero General Public
 *  License as published by the          License” telle que publiée
 *  Free Software Foundation,            par la Free Software Foundation
 *  either version 3 of the              : soit la version 3 de cette
 *  License, or (at your option)         licence, soit (à votre gré)
 *  any later version.                   toute version ultérieure.
 *
 *  OpenCADC is distributed in the       OpenCADC est distribué
 *  hope that it will be useful,         dans l’espoir qu’il vous
 *  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
 *  without even the implied             GARANTIE : sans même la garantie
 *  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
 *  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
 *  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
 *  General Public License for           Générale Publique GNU Affero
 *  more details.                        pour plus de détails.
 *
 *  You should have received             Vous devriez avoir reçu une
 *  a copy of the GNU Affero             copie de la Licence Générale
 *  General Public License along         Publique GNU Affero avec
 *  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
 *  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
 *                                       <http://www.gnu.org/licenses/>.
 *
 ************************************************************************
 */
package ca.nrc.cadc.gms.web.conformance;

import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.jdom.Document;
import org.jdom.Element;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebConversation;


public class UserServiceConformanceTest extends AbstractConformanceTest
{
    private final static Logger LOGGER =
            Logger.getLogger(UserServiceConformanceTest.class);


    @Test
    public void getMember() throws Exception
    {
        final String responsePayload =
                get(getServiceURL() + "/members/" + Long.toString(88l) + "/"
                    + Long.toString(88l));

        LOGGER.info("Response: \r\n" + responsePayload);

        final Document document = buildDocument(responsePayload, false);
        final Element rootUserElement = document.getRootElement();

        assertNotNull("The User should be a part of the given Group.",
                      rootUserElement);
        assertEquals("The Root Element name should be member.", "member",
                     rootUserElement.getName());

        final Element usernameElement = rootUserElement.getChild("username");
        assertNotNull("The username Element should be present.",
                      usernameElement);
        assertEquals("The username Element should be jenkinsd.", "jenkinsd",
                     usernameElement.getValue());
    }

    @Test
    public void getNonMember() throws Exception
    {
        final WebConversation webConversation = new WebConversation();
        final WebRequest getRequest =
                new GetMethodWebRequest(getServiceURL() + "/members/"
                                        + Long.toString(99l) + "/"
                                        + Long.toString(88l));
        webConversation.clearContents();

        final WebResponse response = webConversation.getResource(getRequest);
        assertEquals("Should be a 404 Not Found.", 404,
                     response.getResponseCode());
    }

    @Test
    public void getMemberGroups() throws Exception
    {
        final WebConversation webConversation = new WebConversation();
        final WebRequest getRequest =
                new GetMethodWebRequest(getServiceURL() + "/members/"
                                        + Long.toString(88l));
        webConversation.clearContents();

        final WebResponse response = webConversation.getResource(getRequest);
        assertEquals("Should be a 501 Not Implemented.", 501,
                     response.getResponseCode());
    }
}
