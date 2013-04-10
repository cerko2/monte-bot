## Copyright (C) 2004, 2006, 2007, 2008, 2009 David Bateman and Andy Adler
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
## @deftypefn {Function File} {} sprandsym (@var{n}, @var{d})
## @deftypefnx {Function File} {} sprandsym (@var{s})
## Generate a symmetric random sparse matrix.  The size of the matrix will be
## @var{n} by @var{n}, with a density of values given by @var{d}.
## @var{d} should be between 0 and 1. Values will be normally
## distributed with mean of zero and variance 1.
##
## Note: sometimes the actual density may be a bit smaller than @var{d}. 
## This is unlikely to happen for large really sparse matrices.
##
## If called with a single matrix argument, a random sparse matrix is
## generated wherever the matrix @var{S} is non-zero in its lower
## triangular part.
## @seealso{sprand, sprandn}
## @end deftypefn

function S = sprandsym (n, d)
  if (nargin == 1)
    [i, j, v] = find (tril (n));
    [nr, nc] = size (n);
    S = sparse (i, j, randn (size (v)), nr, nc);
    S = S + tril (S, -1)';
  elseif (nargin == 2)
    m1 = floor (n/2);
    n1 = m1 + rem (n, 2);
    mn1 = m1*n1;
    k1 = round (d*mn1);
    idx1 = unique (fix (rand (min (k1*1.01, k1+10), 1) * mn1)) + 1; 
    ## idx contains random numbers in [1,mn] generate 1% or 10 more
    ## random values than necessary in order to reduce the probability
    ## that there are less than k distinct values; maybe a better
    ## strategy could be used but I don't think it's worth the price.

    ## Actual number of entries in S.
    k1 = min (length (idx1), k1);
    j1 = floor ((idx1(1:k1)-1)/m1);
    i1 = idx1(1:k1) - j1*m1;

    n2 = ceil (n/2);
    nn2 = n2*n2;
    k2 = round (d*nn2);
    idx2 = unique (fix (rand (min (k2*1.01, k1+10), 1) * nn2)) + 1; 
    k2 = min (length (idx2), k2);
    j2 = floor ((idx2(1:k2)-1)/n2);
    i2 = idx2(1:k2) - j2*n2;

    if (isempty (i1) && isempty (i2))
      S = sparse (n, n);
    else
      S1 = sparse (i1, j1+1, randn (k1, 1), m1, n1);
      S = [tril(S1), sparse(m1,m1); ...
	   sparse(i2,j2+1,randn(k2,1),n2,n2), triu(S1,1)'];
      S = S + tril (S, -1)';
    endif
  else
    print_usage ();
  endif
endfunction
