--------------------------------------------------------------------------
--
--  Copyright (C) 2009                          Copyright (C) 2009
--  National Research Council           Conseil national de recherches
--  Ottawa, Canada, K1A 0R6                     Ottawa, Canada, K1A 0R6
--  All rights reserved                         Tous droits reserves
--
--  NRC disclaims any warranties,       Le CNRC denie toute garantie
--  expressed, implied, or statu-       enoncee, implicite ou legale,
--  tory, of any kind with respect      de quelque nature que se soit,
--  to the software, including          concernant le logiciel, y com-
--  without limitation any war-         pris sans restriction toute
--  ranty of merchantability or         garantie de valeur marchande
--  fitness for a particular pur-       ou de pertinence pour un usage
--  pose.  NRC shall not be liable      particulier.  Le CNRC ne
--  in any event for any damages,       pourra en aucun cas etre tenu
--  whether direct or indirect,         responsable de tout dommage,
--  special or general, consequen-      direct ou indirect, particul-
--  tial or incidental, arising         ier ou general, accessoire ou
--  from the use of the software.       fortuit, resultant de l'utili-
--                                                              sation du logiciel.
--
--
--  This file is part of cadcUWS.
--
--  cadcUWS is free software: you can redistribute it and/or modify
--  it under the terms of the GNU Affero General Public License as published by
--  the Free Software Foundation, either version 3 of the License, or
--  (at your option) any later version.
--
--  cadcUWS is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--  GNU Affero General Public License for more details.
--
--  You should have received a copy of the GNU Affero General Public License
--  along with cadcUWS.  If not, see <http://www.gnu.org/licenses/>.
--
--------------------------------------------------------------------------


create table jobs
(
    job_id                    bigint      identity,
    execution_phase           varchar(10) not null,
    execution_duration_sec    bigint      not null,
    destruction_time          datetime    not null,
    quote                     bigint      not null,
    start_time                datetime    null,
    end_time                  datetime    null,
    run_id                    varchar(32) null,
    owner                     varchar(32) null
)
lock datarows
go
 
alter table jobs add constraint jobs_pk primary key (job_id)
go

grant all on jobs to public
go
