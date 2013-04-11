## Copyright (C) 2008, 2009 Ben Abbott
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
## @deftypefn {Function File} {@var{y} =} prctile (@var{x}, @var{p})
## @deftypefnx {Function File} {@var{q} =} prctile (@var{x}, @var{p}, @var{dim})
## For a sample @var{x}, compute the quantiles, @var{y}, corresponding
## to the cumulative probability values, P, in percent.  All non-numeric
## values (NaNs) of X are ignored.
## 
## If @var{x} is a matrix, compute the percentiles for each column and
## return them in a matrix, such that the i-th row of @var{y} contains the 
## @var{p}(i)th percentiles of each column of @var{x}.
## 
## The optional argument @var{dim} determines the dimension along which
## the percentiles are calculated.  If @var{dim} is omitted, and @var{x} is
## a vector or matrix, it defaults to 1 (column wise quantiles).  In the 
## instance that @var{x} is a N-d array, @var{dim} defaults to the first 
## dimension whose size greater than unity.
## 
## @end deftypefn

## Author: Ben Abbott <bpabbott@mac.com>
## Description: Matlab style prctile function.

function q = prctile (x, p, dim)

  if (nargin < 1 || nargin > 3)
    print_usage ();
  endif

  if (nargin < 2)
    p = 100*[0.00 0.25, 0.50, 0.75, 1.00];
  endif

  if (nargin < 3)
    if (ndims (x) == 2)
      ## If a matrix or vector, use the 1st dimension.
      dim = 1;
    else 
      ## If an N-d array, use the firt dimension with a length > 1.
      dim = find (size(v) != 1);
      if (isempty (dim))
        dim = 1;
      endif
    endif
  endif

  ## Convert from percent.
  p = 0.01 * p;

  ## The 5th method is compatible with Matlab.
  method = 5;

  ## Call the quantile function
  q = quantile (x, p, dim, method);

endfunction

%!test
%! pct = 50;
%! q = prctile (1:4, pct, 1);
%! qa = [1, 2, 3, 4];
%! assert (q, qa);
%! q = prctile (1:4, pct, 2);
%! qa = 2.5000;
%! assert (q, qa);

%!test
%! pct = 50;
%! x = [0.1126, 0.1148, 0.0521, 0.2364, 0.1393
%!      0.1718, 0.7273, 0.2041, 0.4531, 0.1585
%!      0.2795, 0.7978, 0.3296, 0.5567, 0.7307
%!      0.4288, 0.8753, 0.6477, 0.6287, 0.8165
%!      0.9331, 0.9312, 0.9635, 0.7796, 0.8461];
%! tol = 0.0001;
%! q = prctile (x, pct, 1);
%! qa = [0.2795, 0.7978, 0.3296, 0.5567, 0.7307];
%! assert (q, qa, tol);
%! q = prctile (x, pct, 2);
%! qa = [0.1148; 0.2041; 0.5567; 0.6477; 0.9312];
%! assert (q, qa, tol);

%!test
%! pct = 50;
%! tol = 0.0001;
%! x = [0.1126, 0.1148, 0.0521, 0.2364, 0.1393
%!      0.1718, 0.7273, 0.2041, 0.4531, 0.1585
%!      0.2795, 0.7978, 0.3296, 0.5567, 0.7307
%!      0.4288, 0.8753, 0.6477, 0.6287, 0.8165
%!      0.9331, 0.9312, 0.9635, 0.7796, 0.8461];
%! x(5,5) = Inf;
%! q = prctile (x, pct, 1);
%! qa = [0.2795, 0.7978, 0.3296, 0.5567, 0.7307];
%! assert (q, qa, tol);
%! x(5,5) = -Inf;
%! q = prctile (x, pct, 1);
%! qa = [0.2795, 0.7978, 0.3296, 0.5567, 0.1585];
%! assert (q, qa, tol);
%! x(1,1) = Inf;
%! q = prctile (x, pct, 1);
%! qa = [0.4288, 0.7978, 0.3296, 0.5567, 0.1585];
%! assert (q, qa, tol);

%!test
%! pct = 50;
%! tol = 0.0001;
%! x = [0.1126, 0.1148, 0.0521, 0.2364, 0.1393
%!      0.1718, 0.7273, 0.2041, 0.4531, 0.1585
%!      0.2795, 0.7978, 0.3296, 0.5567, 0.7307
%!      0.4288, 0.8753, 0.6477, 0.6287, 0.8165
%!      0.9331, 0.9312, 0.9635, 0.7796, 0.8461];
%! x(3,3) = Inf;
%! q = prctile (x, pct, 1);
%! qa = [0.2795, 0.7978, 0.6477, 0.5567, 0.7307];
%! assert (q, qa, tol);
%! q = prctile (x, pct, 2);
%! qa = [0.1148; 0.2041; 0.7307; 0.6477; 0.9312];
%! assert (q, qa, tol);

%!test
%! pct = 50;
%! tol = 0.0001;
%! x = [0.1126, 0.1148, 0.0521, 0.2364, 0.1393
%!      0.1718, 0.7273, 0.2041, 0.4531, 0.1585
%!      0.2795, 0.7978, 0.3296, 0.5567, 0.7307
%!      0.4288, 0.8753, 0.6477, 0.6287, 0.8165
%!      0.9331, 0.9312, 0.9635, 0.7796, 0.8461];
%! x(5,5) = NaN;
%! q = prctile (x, pct, 2);
%! qa = [0.1148; 0.2041; 0.5567; 0.6477; 0.9322];
%! assert (q, qa, tol);
%! x(1,1) = NaN;
%! q = prctile (x, pct, 2);
%! qa = [0.1270; 0.2041; 0.5567; 0.6477; 0.9322];
%! assert (q, qa, tol);
%! x(3,3) = NaN;
%! q = prctile (x, pct, 2);
%! qa = [0.1270; 0.2041; 0.6437; 0.6477; 0.9322];
%! assert (q, qa, tol);

