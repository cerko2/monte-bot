## Copyright (C) 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2004, 2005,
##               2006, 2007, 2008, 2009 John W. Eaton
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
## @deftypefn {Function File} {[@var{x}, @var{map}] =} loadimage (@var{file})
## Load an image file and its associated color map from the specified
## @var{file}.  The image must be stored in Octave's image format.
## @seealso{saveimage, load, save}
## @end deftypefn

## Author: Tony Richardson <arichard@stark.cc.oh.us>
## Created: July 1994
## Adapted-By: jwe

## Deprecated in version 3.2

function [img_retval, map_retval] = loadimage (varargin)

  persistent warned = false;
  if (! warned)
    warned = true;
    warning ("Octave:deprecated-function",
             "loadimage is obsolete and will be removed from a future version of Octave; please use imread instead");
  endif

  [img_retval, map_retval] = imread (varargin{:});

endfunction
