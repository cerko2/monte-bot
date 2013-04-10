## Copyright (C) 1995, 1996, 1997, 1998, 2000, 2002, 2005, 2006, 2007, 2009
##               Kurt Hornik
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
## @deftypefn {Function File} {} cor_test (@var{x}, @var{y}, @var{alt}, @var{method})
## Test whether two samples @var{x} and @var{y} come from uncorrelated
## populations.
##
## The optional argument string @var{alt} describes the alternative
## hypothesis, and can be @code{"!="} or @code{"<>"} (non-zero),
## @code{">"} (greater than 0), or @code{"<"} (less than 0).  The
## default is the two-sided case.
##
## The optional argument string @var{method} specifies on which
## correlation coefficient the test should be based.  If @var{method} is
## @code{"pearson"} (default), the (usual) Pearson's product moment
## correlation coefficient is used.  In this case, the data should come
## from a bivariate normal distribution.  Otherwise, the other two
## methods offer nonparametric alternatives.  If @var{method} is
## @code{"kendall"}, then Kendall's rank correlation tau is used.  If
## @var{method} is @code{"spearman"}, then Spearman's rank correlation
## rho is used.  Only the first character is necessary.
##
## The output is a structure with the following elements:
##
## @table @var
## @item pval
## The p-value of the test.
## @item stat
## The value of the test statistic.
## @item dist
## The distribution of the test statistic.
## @item params
## The parameters of the null distribution of the test statistic.
## @item alternative
## The alternative hypothesis.
## @item method
## The method used for testing.
## @end table
##
## If no output argument is given, the p-value is displayed.
## @end deftypefn

## Author: FL <Friedrich.Leisch@ci.tuwien.ac.at>
## Adapted-by: KH <Kurt.Hornik@wu-wien.ac.at>
## Description: Test for zero correlation

function t = cor_test (X, Y, ALTERNATIVE, METHOD)

  if ((nargin < 2) || (nargin > 4))
    print_usage ();
  endif

  if (!isvector (X) || !isvector (Y) || length (X) != length (Y))
    error ("cor_test: X and Y must be vectors of the same length");
  endif

  if (nargin < 3)
    ALTERNATIVE = "!=";
  elseif (! ischar (ALTERNATIVE))
    error ("cor_test: ALTERNATIVE must be a string");
  endif

  if (nargin < 4)
    METHOD = "pearson";
  elseif (! ischar (METHOD))
    error ("cor_test: METHOD must be a string");
  endif

  n = length (X);
  m = METHOD (1);

  if (m == "p")
    r = cor (X, Y);
    df = n - 2;
    t.method = "Pearson's product moment correlation";
    t.params = df;
    t.stat = sqrt (df) .* r / sqrt (1 - r.^2);
    t.dist = "t";
    cdf  = t_cdf (t.stat, df);
  elseif (m == "k")
    tau = kendall (X, Y);
    t.method = "Kendall's rank correlation tau";
    t.params = [];
    t.stat = tau / sqrt ((2 * (2*n+5)) / (9*n*(n-1)));
    t.dist = "stdnormal";
    cdf = stdnormal_cdf (t.stat);
  elseif (m == "s")
    rho = spearman (X, Y);
    t.method = "Spearman's rank correlation rho";
    t.params = [];
    t.stat = sqrt (n-1) * (rho - 6/(n^3-n));
    t.dist = "stdnormal";
    cdf = stdnormal_cdf (t.stat);
  else
    error ("cor_test: method `%s' not recognized", METHOD);
  endif

  if (strcmp (ALTERNATIVE, "!=") || strcmp (ALTERNATIVE, "<>"))
    t.pval = 2 * min (cdf, 1 - cdf);
  elseif (strcmp (ALTERNATIVE, ">"))
    t.pval = 1 - cdf;
  elseif (strcmp (ALTERNATIVE, "<"))
    t.pval = cdf;
  else
    error ("cor_test: alternative `%s' not recognized", ALTERNATIVE);
  endif

  t.alternative = ALTERNATIVE;

  if (nargout == 0)
    printf ("pval: %g\n", t.pval);
  endif

endfunction
