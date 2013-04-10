## Copyright (C) 2000, 2006, 2007, 2009 Kai Habel
## Copyright (C) 2008 Marco Caliari
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
## @deftypefn {Function File} {@var{l} =} legendre (@var{n}, @var{x})
## @deftypefnx {Function File} {@var{l} =} legendre (@var{n}, @var{x}, @var{normalization})
## Compute the Legendre function of degree @var{n} and order 
## @var{m} = 0 @dots{} N.  The optional argument, @var{normalization}, 
## may be one of @code{"unnorm"}, @code{"sch"}, or @code{"norm"}.
## The default is @code{"unnorm"}.  The value of @var{n} must be a 
## non-negative scalar integer.  
##
## If the optional argument @var{normalization} is missing or is
## @code{"unnorm"}, compute the Legendre function of degree @var{n} and
## order @var{m} and return all values for @var{m} = 0 @dots{} @var{n}.
## The return value has one dimension more than @var{x}.
##
## The Legendre Function of degree @var{n} and order @var{m}:
##
## @example
## @group
##  m        m       2  m/2   d^m
## P(x) = (-1) * (1-x  )    * ----  P (x)
##  n                         dx^m   n
## @end group
## @end example
##
## @noindent
## with Legendre polynomial of degree @var{n}:
##
## @example
## @group
##           1     d^n   2    n
## P (x) = ------ [----(x - 1)  ]
##  n      2^n n!  dx^n
## @end group
## @end example
##
## @noindent
## @code{legendre (3, [-1.0, -0.9, -0.8])} returns the matrix:
##
## @example
## @group
##  x  |   -1.0   |   -0.9   |  -0.8
## ------------------------------------
## m=0 | -1.00000 | -0.47250 | -0.08000
## m=1 |  0.00000 | -1.99420 | -1.98000
## m=2 |  0.00000 | -2.56500 | -4.32000
## m=3 |  0.00000 | -1.24229 | -3.24000 
## @end group
## @end example
##
## If the optional argument @code{normalization} is @code{"sch"}, 
## compute the Schmidt semi-normalized associated Legendre function.
## The Schmidt semi-normalized associated Legendre function is related
## to the unnormalized Legendre functions by the following:
##
## For Legendre functions of degree n and order 0:
##
## @example
## @group
##   0       0
## SP (x) = P (x)
##   n       n
## @end group
## @end example
##
## For Legendre functions of degree n and order m:
##
## @example
## @group
##   m       m          m    2(n-m)! 0.5
## SP (x) = P (x) * (-1)  * [-------]
##   n       n               (n+m)!
## @end group
## @end example
##
## If the optional argument @var{normalization} is @code{"norm"}, 
## compute the fully normalized associated Legendre function.
## The fully normalized associated Legendre function is related
## to the unnormalized Legendre functions by the following:
##
## For Legendre functions of degree @var{n} and order @var{m}
##
## @example
## @group
##   m       m          m    (n+0.5)(n-m)! 0.5
## NP (x) = P (x) * (-1)  * [-------------]
##   n       n                   (n+m)!    
## @end group
## @end example
## @end deftypefn

## Author: Marco Caliari <marco.caliari@univr.it>

function retval = legendre (n, x, normalization)

  persistent warned_overflow = false;

  if (nargin < 2 || nargin > 3)
    print_usage ();
  endif

  if (nargin == 3)
    normalization = lower (normalization);
  else
    normalization = "unnorm";
  endif

  if (! isscalar (n) || n < 0 || n != fix (n))
    error ("legendre: n must be a non-negative scalar integer");
  endif

  if (! isvector (x) || any (x < -1 || x > 1))
    error ("legendre: x must be vector in range -1 <= x <= 1");
  endif

  switch (normalization)
    case "norm"
      scale = sqrt (n+0.5);
    case "sch"
      scale = sqrt (2);
    case "unnorm"
      scale = 1;
    otherwise
      error ("legendre: expecting normalization option to be \"norm\", \"sch\", or \"unnorm\"");
  endswitch

  scale = scale * ones (1, numel (x));

  ## Based on the recurrence relation below
  ##            m                 m              m
  ## (n-m+1) * P (x) = (2*n+1)*x*P (x)  - (n+1)*P (x)
  ##            n+1               n              n-1
  ## http://en.wikipedia.org/wiki/Associated_Legendre_function

  overflow = false;
  for m = 1:n
    lpm1 = scale;
    lpm2 = (2*m-1) .* x .* scale;      
    lpm3 = lpm2;
    for k = m+1:n
      lpm3a = (2*k-1) .* x .* lpm2;
      lpm3b = (k+m-2) .* lpm1;
      lpm3 = (lpm3a - lpm3b)/(k-m+1);
      lpm1 = lpm2;
      lpm2 = lpm3;
      if (! warned_overflow)
        if (any (abs (lpm3a) > realmax)
            || any (abs (lpm3b) > realmax)
            || any (abs (lpm3)  > realmax))
          overflow = true;
        endif
      endif
    endfor
    retval(m,:) = lpm3;
    if (strcmp (normalization, "unnorm"))
      scale = -scale * (2*m-1);
    else
      ## normalization == "sch" or normalization == "norm"
      scale = scale / sqrt ((n-m+1)*(n+m))*(2*m-1);
    endif
    scale = scale .* sqrt(1-x.^2);
  endfor

  retval(n+1,:) = scale;

  if (strcmp (normalization, "sch"))
    retval(1,:) = retval(1,:) / sqrt (2);
  endif

  if (overflow && ! warned_overflow)
    warning ("legendre: overflow - results may be unstable for high orders");
    warned_overflow = true;
  endif

endfunction

%!test
%! result = legendre (3, [-1.0 -0.9 -0.8]);
%! expected = [
%!    -1.00000  -0.47250  -0.08000
%!     0.00000  -1.99420  -1.98000
%!     0.00000  -2.56500  -4.32000
%!     0.00000  -1.24229  -3.24000
%! ];
%! assert (result, expected, 1e-5);

%!test
%! result = legendre (3, [-1.0 -0.9 -0.8], "sch");
%! expected = [
%!    -1.00000  -0.47250  -0.08000
%!     0.00000   0.81413   0.80833
%!    -0.00000  -0.33114  -0.55771
%!     0.00000   0.06547   0.17076
%! ];
%! assert (result, expected, 1e-5);

%!test
%! result = legendre (3, [-1.0 -0.9 -0.8], "norm");
%! expected = [
%!    -1.87083  -0.88397  -0.14967
%!     0.00000   1.07699   1.06932
%!    -0.00000  -0.43806  -0.73778
%!     0.00000   0.08661   0.22590
%! ];
%! assert (result, expected, 1e-5);

%!test
%! result = legendre (151, 0);
%! ## Don't compare to "-Inf" since it would fail on 64 bit systems.
%! assert (result(end) < -1.7976e308 && all (isfinite (result(1:end-1))));

%!test
%! result = legendre (150, 0);
%! ## This agrees with Matlab's result.
%! assert (result(end), 3.7532741115719e+306, 0.0000000000001e+306)

%!test
%! result = legendre (0, 0:0.1:1);
%! assert (result, full(ones(1,11)))
