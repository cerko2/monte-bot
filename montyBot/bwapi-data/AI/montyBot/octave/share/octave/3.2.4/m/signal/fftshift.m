## Copyright (C) 1997, 1998, 2000, 2002, 2004, 2005, 2006, 2007, 2009
##               Vincent Cautaerts
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
## @deftypefn {Function File} {} fftshift (@var{v})
## @deftypefnx {Function File} {} fftshift (@var{v}, @var{dim})
## Perform a shift of the vector @var{v}, for use with the @code{fft}
## and @code{ifft} functions, in order the move the frequency 0 to the
## center of the vector or matrix.
##
## If @var{v} is a vector of @math{N} elements corresponding to @math{N}
## time samples spaced of @math{Dt} each, then @code{fftshift (fft
## (@var{v}))} corresponds to frequencies
##
## @example
## f = ((1:N) - ceil(N/2)) / N / Dt
## @end example
##
## If @var{v} is a matrix, the same holds for rows and columns.  If 
## @var{v} is an array, then the same holds along each dimension.
##
## The optional @var{dim} argument can be used to limit the dimension
## along which the permutation occurs.
## @end deftypefn

## Author: Vincent Cautaerts <vincent@comf5.comm.eng.osaka-u.ac.jp>
## Created: July 1997
## Adapted-By: jwe

function retval = fftshift (V, dim)

  retval = 0;

  if (nargin != 1 && nargin != 2)
    print_usage ();
  endif

  if (nargin == 2)
    if (!isscalar (dim))
      error ("fftshift: dimension must be an integer scalar");
    endif
    nd = ndims (V);
    sz = size (V);
    sz2 = ceil (sz(dim) / 2);
    idx = cell ();
    for i = 1:nd
      idx{i} = 1:sz(i);
    endfor
    idx{dim} = [sz2+1:sz(dim), 1:sz2];
    retval = V (idx{:});
  else
    if (isvector (V))
      x = length (V);
      xx = ceil (x/2);
      retval = V([xx+1:x, 1:xx]);
    elseif (ismatrix (V))
      nd = ndims (V);
      sz = size (V);
      sz2 = ceil (sz ./ 2);
      idx = cell ();
      for i = 1:nd
        idx{i} = [sz2(i)+1:sz(i), 1:sz2(i)];
      endfor
      retval = V (idx{:});
    else
      error ("fftshift: expecting vector or matrix argument");
    endif
  endif

endfunction
