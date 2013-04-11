## Copyright (C) 2000, 2001, 2003, 2005, 2006, 2007 Paul Kienzle
##
## This file is part of Octave.
##
## Octave is free software; you can redistribute it and/or modify it
## under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or (at
## your option) any later version.
##
## Octave is distributed in the hope that it will be useful, but
## WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
## General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## -*- texinfo -*-
## @deftypefn {Function File} {t =} now ()
## Returns the current local time as the number of days since Jan 1, 0000.
## By this reckoning, Jan 1, 1970 is day number 719529.
##
## The integral part, @code{floor (now)} corresponds to 00:00:00 today.
##
## The fractional part, @code{rem (now, 1)} corresponds to the current
## time on Jan 1, 0000.
##
## The returned value is also called a "serial date number"
## (see @code{datenum}).
## @seealso{clock, date, datenum}
## @end deftypefn

## Author: pkienzle <pkienzle@users.sf.net>
## Created: 10 October 2001 (CVS)
## Adapted-By: William Poetra Yoga Hadisoeseno <williampoetra@gmail.com>

function t = now ()

  t = datenum (clock ());

  ## The following doesn't work (e.g., one hour off on 2005-10-04):
  ##
  ##   seconds since 1970-1-1 corrected by seconds from GMT to local time
  ##   divided by 86400 sec/day plus day num for 1970-1-1
  ##   t = (time - mktime(gmtime(0)))/86400 + 719529;
  ##
  ## mktime(gmtime(0)) does indeed return the offset from Greenwich to the
  ## local time zone, but we need to account for daylight savings time
  ## changing by an hour the offset from CUT for part of the year.

endfunction
