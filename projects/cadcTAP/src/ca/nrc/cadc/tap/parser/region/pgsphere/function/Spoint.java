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

package ca.nrc.cadc.tap.parser.region.pgsphere.function;

import java.util.ArrayList;
import java.util.List;

import ca.nrc.cadc.stc.Circle;
import ca.nrc.cadc.stc.CoordPair;
import ca.nrc.cadc.stc.Position;
import ca.nrc.cadc.tap.parser.RegionFinder;
import ca.nrc.cadc.tap.parser.region.pgsphere.expression.DegreeDouble;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

/**
 * the PgSphere implementation of ADQL function
 * POINT
 * 
 * @author zhangsa
 * 
 */
public class Spoint extends PgsFunction
{
    private Expression _longitude;
    private Expression _latitude;

    private boolean isOperand;

    public Spoint(Function adqlFunction)
    {
        super(adqlFunction);
        convertParameters();
    }

    public Spoint(Expression longitude, Expression latitude)
    {
        _longitude = longitude;
        _latitude = latitude;
    }

    public Spoint(Position position)
    {
        double ra, dec;
        List<Expression> expressions = new ArrayList<Expression>();
        expressions.add(new StringValue(RegionFinder.ICRS));
        CoordPair cp = position.getCoordPair();
        ra = cp.getX();
        dec = cp.getY();
        expressions.add(new DegreeDouble(ra));
        expressions.add(new DegreeDouble(dec));
        ExpressionList el = new ExpressionList(expressions);
        this.setParameters(el);
        convertParameters();
    }


    @SuppressWarnings("unchecked")
    protected void convertParameters()
    {
        List<Expression> params = this.getParameters().getExpressions();
        _longitude = params.get(1);
        _latitude = params.get(2);

        List<Expression> pgsParams = new ArrayList<Expression>(2);
        pgsParams.add(_longitude);
        pgsParams.add(_latitude);
        ExpressionList pgsParamExprList = new ExpressionList(pgsParams);
        setParameters(pgsParamExprList);
    }

    public void setIsOperand(boolean isOp)
    {
        this.isOperand = isOp;
    }
    
    @Override
    public String toString()
    {
        String ret = "spoint '(" + _longitude + "," + _latitude + ")'";
        if (isOperand)
            ret = "cast(" + ret + " as scircle)";
        return ret;
    }

    public String valueString()
    {
        return "(" + _longitude + "," + _latitude + ")";
    }
}
