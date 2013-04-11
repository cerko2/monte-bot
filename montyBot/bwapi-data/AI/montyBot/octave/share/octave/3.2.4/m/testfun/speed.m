## Copyright (C) 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008,
##               2009 Paul Kienzle
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
## @deftypefn {Function File} {} speed (@var{f}, @var{init}, @var{max_n}, @var{f2}, @var{tol})
## @deftypefnx {Function File} {[@var{order}, @var{n}, @var{T_f}, @var{T_f2}] =} speed (@dots{})
##
## Determine the execution time of an expression for various @var{n}.
## The @var{n} are log-spaced from 1 to @var{max_n}.  For each @var{n},
## an initialization expression is computed to create whatever data
## are needed for the test.  If a second expression is given, the
## execution times of the two expressions will be compared.  Called 
## without output arguments the results are presented graphically.
##
## @table @code
## @item @var{f}
## The expression to evaluate.
##
## @item @var{max_n}
## The maximum test length to run.  Default value is 100.  Alternatively,
## use @code{[min_n,max_n]} or for complete control, @code{[n1,n2,@dots{},nk]}.
##
## @item @var{init}
## Initialization expression for function argument values.  Use @var{k} 
## for the test number and @var{n} for the size of the test.  This should
## compute values for all variables listed in args.  Note that init will
## be evaluated first for @math{k = 0}, so things which are constant throughout
## the test can be computed then.  The default value is @code{@var{x} =
## randn (@var{n}, 1);}.
##
## @item @var{f2}
## An alternative expression to evaluate, so the speed of the two
## can be compared.  Default is @code{[]}.
##
## @item @var{tol}
## If @var{tol} is @code{Inf}, then no comparison will be made between the
## results of expression @var{f} and expression @var{f2}.  Otherwise,
## expression @var{f} should produce a value @var{v} and expression @var{f2} 
## should produce a value @var{v2}, and these shall be compared using 
## @code{assert(@var{v},@var{v2},@var{tol})}.  If @var{tol} is positive,
## the tolerance is assumed to be absolute.  If @var{tol} is negative,
## the tolerance is assumed to be relative.  The default is @code{eps}.
##
## @item @var{order}
## The time complexity of the expression @code{O(a n^p)}.  This
## is a structure with fields @code{a} and @code{p}.
##
## @item @var{n}
## The values @var{n} for which the expression was calculated and
## the execution time was greater than zero.
##
## @item @var{T_f}
## The nonzero execution times recorded for the expression @var{f} in seconds.
##
## @item @var{T_f2}
## The nonzero execution times recorded for the expression @var{f2} in seconds.
## If it is needed, the mean time ratio is just @code{mean(T_f./T_f2)}.
##
## @end table
##
## The slope of the execution time graph shows the approximate
## power of the asymptotic running time @code{O(n^p)}.  This
## power is plotted for the region over which it is approximated
## (the latter half of the graph).  The estimated power is not
## very accurate, but should be sufficient to determine the
## general order of your algorithm.  It should indicate if for 
## example your implementation is unexpectedly @code{O(n^2)} 
## rather than @code{O(n)} because it extends a vector each 
## time through the loop rather than preallocating one which is 
## big enough.  For example, in the current version of Octave,
## the following is not the expected @code{O(n)}:
##
## @example
## speed ("for i = 1:n, y@{i@} = x(i); end", "", [1000,10000])
## @end example
##
## but it is if you preallocate the cell array @code{y}:
##
## @example
## @group
## speed ("for i = 1:n, y@{i@} = x(i); end", ...
##        "x = rand (n, 1); y = cell (size (x));", [1000, 10000])
## @end group
## @end example
##
## An attempt is made to approximate the cost of the individual 
## operations, but it is wildly inaccurate.  You can improve the
## stability somewhat by doing more work for each @code{n}.  For
## example:
##
## @example
## speed ("airy(x)", "x = rand (n, 10)", [10000, 100000])
## @end example
##
## When comparing a new and original expression, the line on the
## speedup ratio graph should be larger than 1 if the new expression
## is faster.  Better algorithms have a shallow slope.  Generally, 
## vectorizing an algorithm will not change the slope of the execution 
## time graph, but it will shift it relative to the original.  For
## example:
##
## @example
## @group
## speed ("v = sum (x)", "", [10000, 100000], ...
##        "v = 0; for i = 1:length (x), v += x(i); end")
## @end group
## @end example
## 
## A more complex example, if you had an original version of @code{xcorr}
## using for loops and another version using an FFT, you could compare the
## run speed for various lags as follows, or for a fixed lag with varying
## vector lengths as follows:
##
## @example
## @group
## speed ("v = xcorr (x, n)", "x = rand (128, 1);", 100,
##        "v2 = xcorr_orig (x, n)", -100*eps)
## speed ("v = xcorr (x, 15)", "x = rand (20+n, 1);", 100,
##        "v2 = xcorr_orig (x, n)", -100*eps)
## @end group
## @end example
##
## Assuming one of the two versions is in @var{xcorr_orig}, this
## would compare their speed and their output values.  Note that the
## FFT version is not exact, so we specify an acceptable tolerance on
## the comparison @code{100*eps}, and the errors should be computed
## relatively, as @code{abs((@var{x} - @var{y})./@var{y})} rather than 
## absolutely as @code{abs(@var{x} - @var{y})}.
##
## Type @code{example('speed')} to see some real examples.  Note for 
## obscure reasons, you can't run examples 1 and 2 directly using 
## @code{demo('speed')}.  Instead use, @code{eval(example('speed',1))}
## and @code{eval(example('speed',2))}.
## @end deftypefn

## FIXME: consider two dimensional speedup surfaces for functions like kron.
function [__order, __test_n, __tnew, __torig] ...
      = speed (__f1, __init, __max_n, __f2, __tol)

  if (nargin < 1 || nargin > 6)
    print_usage ();
  endif

  if (nargin < 2 || isempty (__init))
    __init = "x = randn(n, 1);";
  endif

  if (nargin < 3 || isempty (__max_n))
    __max_n = 100;
  endif

  if (nargin < 4)
    __f2 = [];
  endif

  if (nargin < 5 || isempty (__tol))
    __tol = eps;
  endif

  __numtests = 15;

  ## Let user specify range of n.
  if (isscalar (__max_n))
    __min_n = 1;
    assert (__max_n > __min_n);
    __test_n = logspace (0, log10 (__max_n), __numtests);
  elseif (length (__max_n) == 2)
    __min_n = __max_n(1);
    __max_n = __max_n(2);
    assert (__min_n >= 1);
    __test_n = logspace (log10 (__min_n), log10 (__max_n), __numtests);
  else
    __test_n = __max_n;
  endif
  ## Force n to be an integer.
  __test_n = unique (round (__test_n));
  assert (__test_n >= 1);

  __torig = __tnew = zeros (size (__test_n));

  disp (cstrcat ("testing ", __f1, "\ninit: ", __init));

  ## Make sure the functions are freshly loaded by evaluating them at
  ## test_n(1); first have to initialize the args though.
  n = 1;
  k = 0;
  eval (cstrcat (__init, ";"));
  if (! isempty (__f2))
    eval (cstrcat (__f2, ";"));
  endif
  eval (cstrcat (__f1, ";"));

  ## Run the tests.
  for k = 1:length (__test_n)
    n = __test_n(k);
    eval (cstrcat (__init, ";"));
    
    printf ("n%i = %i  ",k, n);
    fflush (stdout);
    eval (cstrcat ("__t = time();", __f1, "; __v1=ans; __t = time()-__t;"));
    if (__t < 0.25)
      eval (cstrcat ("__t2 = time();", __f1, "; __t2 = time()-__t2;"));
      eval (cstrcat ("__t3 = time();", __f1, "; __t3 = time()-__t3;"));
      __t = min ([__t, __t2, __t3]);
    endif
    __tnew(k) = __t;

    if (! isempty (__f2))
      eval (cstrcat ("__t = time();", __f2, "; __v2=ans; __t = time()-__t;"));
      if (__t < 0.25)
      	eval (cstrcat ("__t2 = time();", __f2, "; __t2 = time()-__t2;"));
      	eval (cstrcat ("__t3 = time();", __f2, "; __t3 = time()-__t3;"));
      endif
      __torig(k) = __t;
      if (! isinf(__tol))
      	assert (__v1, __v2, __tol);
      endif
    endif
  endfor
  
  ## Drop times of zero.
  if (! isempty (__f2))
    zidx = (__tnew < 100*eps |  __torig < 100*eps);
    __test_n(zidx) = [];
    __tnew(zidx) = [];
    __torig(zidx) = [];
  else
    zidx = (__tnew < 100*eps);
    __test_n(zidx) = [];
    __tnew(zidx) = [];
  endif

  ## Approximate time complexity and return it if requested.
  tailidx = ceil(length(__test_n)/2):length(__test_n);
  p = polyfit (log (__test_n(tailidx)), log (__tnew(tailidx)), 1);
  if (nargout > 0) 
    __order.p = p(1);
    __order.a = exp (p(2));
  endif

  ## Plot the data if no output is requested.
  doplot = (nargout == 0);
  
  if (doplot)
    figure;
  endif

  if (doplot && ! isempty (__f2))
    subplot (1, 2, 1);
    semilogx (__test_n, __torig./__tnew, 
	      cstrcat ("-*r;", strrep (__f1, ";", "."), "/",
		      strrep (__f2, ";", "."), ";"),
	       __test_n, __tnew./__torig,
	      cstrcat ("-*g;", strrep (__f2, ";", "."), "/",
		      strrep (__f1, ";", "."), ";"));
    xlabel ("test length");
    title (__f1);
    ylabel ("speedup ratio");

    subplot (1, 2, 2);
    loglog (__test_n, __tnew*1000,
	    cstrcat ("*-g;", strrep (__f1, ";", "."), ";"), 
	    __test_n, __torig*1000,
	    cstrcat ("*-r;", strrep (__f2,";","."), ";"));
  
    xlabel ("test length");
    ylabel ("best execution time (ms)");
    title (cstrcat ("init: ", __init));

    ratio = mean (__torig ./ __tnew);
    printf ("\n\nMean runtime ratio = %.3g for '%s' vs '%s'\n",
            ratio, __f2, __f1);

  elseif (doplot)

    loglog (__test_n, __tnew*1000, "*-g;execution time;");
    xlabel ("test length");
    ylabel ("best execution time (ms)");
    title (cstrcat (__f1, "  init: ", __init));

  endif

  if (doplot)

    ## Plot time complexity approximation (using milliseconds).
    order = sprintf ("O(n^%g)", round (10*p(1))/10);
    v = polyval (p, log (__test_n(tailidx)));

    loglog (__test_n(tailidx), exp(v)*1000, sprintf ("b;%s;", order)); 

    ## Get base time to 1 digit of accuracy.
    dt = exp (p(2));
    dt = floor (dt/10^floor(log10(dt)))*10^floor(log10(dt));
    if (log10 (dt) >= -0.5)
      time = sprintf ("%g s", dt);
    elseif (log10 (dt) >= -3.5)
      time = sprintf ("%g ms", dt*1e3);
    elseif (log10 (dt) >= -6.5)
      time = sprintf ("%g us", dt*1e6);
    else
      time = sprintf ("%g ns", dt*1e9);
    endif

    ## Display nicely formatted complexity.
    printf ("\nFor %s:\n", __f1);
    printf ("  asymptotic power: %s\n", order);
    printf ("  approximate time per operation: %s\n", time); 

  endif

endfunction

%!demo if 1
%!  function x = build_orig(n)
%!    ## extend the target vector on the fly
%!    for i=0:n-1, x([1:10]+i*10) = 1:10; endfor
%!  endfunction
%!  function x = build(n)
%!    ## preallocate the target vector
%!    x = zeros(1, n*10);
%!    try
%!      if (prefer_column_vectors), x = x.'; endif
%!    catch
%!    end
%!    for i=0:n-1, x([1:10]+i*10) = 1:10; endfor
%!  endfunction
%!
%!  disp("-----------------------");
%!  type build_orig;
%!  disp("-----------------------");
%!  type build;
%!  disp("-----------------------");
%!
%!  disp("Preallocated vector test.\nThis takes a little while...");
%!  speed('build(n)', '', 1000, 'build_orig(n)');
%!  clear build build_orig
%!  disp("Note how much faster it is to pre-allocate a vector.");
%!  disp("Notice the peak speedup ratio.");
%! endif

%!demo if 1
%!  function x = build_orig(n)
%!    for i=0:n-1, x([1:10]+i*10) = 1:10; endfor
%!  endfunction
%!  function x = build(n)
%!    idx = [1:10]';
%!    x = idx(:,ones(1,n));
%!    x = reshape(x, 1, n*10);
%!    try
%!      if (prefer_column_vectors), x = x.'; endif
%!    catch
%!    end
%!  endfunction
%!
%!  disp("-----------------------");
%!  type build_orig;
%!  disp("-----------------------");
%!  type build;
%!  disp("-----------------------");
%!
%!  disp("Vectorized test. This takes a little while...");
%!  speed('build(n)', '', 1000, 'build_orig(n)');
%!  clear build build_orig
%!  disp("-----------------------");
%!  disp("This time, the for loop is done away with entirely.");
%!  disp("Notice how much bigger the speedup is then in example 1.");
%! endif
