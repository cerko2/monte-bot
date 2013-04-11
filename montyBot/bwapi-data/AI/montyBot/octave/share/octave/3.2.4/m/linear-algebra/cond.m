## Copyright (C) 1993, 1994, 1995, 1996, 1997, 1999, 2000, 2003, 2004,
##               2005, 2006, 2007, 2008, 2009 John W. Eaton
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
## @deftypefn {Function File} {} cond (@var{a},@var{p})
## Compute the @var{p}-norm condition number of a matrix.  @code{cond (@var{a})} is
## defined as @code{norm (@var{a}, @var{p}) * norm (inv (@var{a}), @var{p})}.
## By default @code{@var{p}=2} is used which implies a (relatively slow)
## singular value decomposition.  Other possible selections are 
## @code{@var{p}= 1, Inf, inf, 'Inf', 'fro'} which are generally faster.
## @seealso{condest, rcond, norm, svd}
## @end deftypefn

## Author: jwe

function retval = cond (a, p)

  if (nargin && nargin < 3)
    if (ndims (a) > 2)
      error ("cond: only valid on 2-D objects");
    endif

    if (nargin <2)
      p = 2;
    endif

    if (! ischar (p) && p == 2)
      [nr, nc] = size (a);
      if (nr == 0 || nc == 0)
        retval = 0.0;
      elseif (any (any (isinf (a) | isnan (a))))
        error ("cond: argument must not contain Inf or NaN values");
      else
        sigma   = svd (a);
        sigma_1 = sigma(1);
        sigma_n = sigma(end);
        if (sigma_1 == 0 || sigma_n == 0)
          retval = Inf;
        else
          retval = sigma_1 / sigma_n;
        endif
      endif
    else
      retval = norm (a, p) * norm (inv (a), p);  
    endif
  else
    print_usage ();
  endif

endfunction

%!test
%! y= [7, 2, 3; 1, 3, 4; 6, 4, 5];
%! tol = 1e-6;
%! type = {1, 2, 'fro', 'inf', inf};
%! for n = 1:numel(type)
%!   rcondition(n) = 1 / cond (y, type{n});
%! endfor
%! assert (rcondition, [0.017460, 0.019597, 0.018714, 0.012022, 0.012022], tol);

%!assert (abs (cond ([1, 2; 2, 1]) - 3) < sqrt (eps));

%!assert (cond ([1, 2, 3; 4, 5, 6; 7, 8, 9]) > 1.0e+16);

%!error cond ();

%!error cond (1, 2, 3);

