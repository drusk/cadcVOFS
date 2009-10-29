/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2009.                            (c) 2009.
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
 *  $Revision: 4 $
 *
 ************************************************************************
 */

/**
 * 
 */
package ca.nrc.cadc.tap.parser.adql;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import net.sf.jsqlparser.statement.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.nrc.cadc.tap.parser.adql.config.meta.TableMeta;
import ca.nrc.cadc.tap.parser.adql.exception.AdqlException;
import ca.nrc.cadc.tap.parser.adql.impl.postgresql.sql.AdqlManagerImpl;
import ca.nrc.cadc.util.LoggerUtil;
/**
 * 
 * @author Sailor Zhang
 * 
 */
public class DbSchemaViewer {
	private AdqlManager _manager;
	private AdqlParser _adqlParser;
	public String adqlInput;
	public String sqlOutput;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		LoggerUtil.initialize(new String[] { "test", "ca.nrc.cadc" }, new String[] { "-d" });
		_manager = new AdqlManagerImpl(null);
		this._adqlParser = new AdqlParser(_manager);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private void doValidate(boolean expectValid) {
		Statement s = null;
		boolean exceptionHappens = false;
		try {
			s = _adqlParser.validate(adqlInput);
		} catch (AdqlException ae) {
			exceptionHappens = true;
			ae.printStackTrace(System.out);
			if (expectValid)
				fail(ae.toString());
			else
				assert (true);
		}
		System.out.println(s);
		if (expectValid) {
			if (exceptionHappens)
				assert (false);
			else
				assert (true);
		} else {
			if (exceptionHappens)
				assert (true);
			else
				assert (false);
		}
	}

	private void doConvert(boolean expectValid) {
		Statement s = null;
		try {
			s = _adqlParser.validate(adqlInput);
			System.out.println(s.toString());
			s = _adqlParser.convert(s);
			System.out.println(s.toString());
		} catch (AdqlException ae) {
			ae.printStackTrace(System.out);
			fail(ae.toString());
		}
	}

	//@Test
	public void testConnection() {
		String jdbcUrl = "jdbc:postgresql://cvodb0/cvodb?user=zhangsa&password=sailor";
		
		Connection conn = null;
		String jdbcClass = "org.postgresql.Driver";

		try {
			Class.forName(jdbcClass).newInstance();
			conn = DriverManager.getConnection(jdbcUrl);
			System.out.println(conn.toString());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testViewSchema() {
		List<TableMeta> tableMetas = _manager.getConfig().getTableMetas();
		for (TableMeta tableMeta : tableMetas) {
			System.out.println(tableMeta);
		}
	}
}
