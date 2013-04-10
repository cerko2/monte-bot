## Copyright (C) 2008, 2009 David Bateman
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
## @deftypefn {Function File} {} quadgk (@var{f}, @var{a}, @var{b}, @var{abstol}, @var{trace})
## @deftypefnx {Function File} {} quadgk (@var{f}, @var{a}, @var{b}, @var{prop}, @var{val}, @dots{})
## @deftypefnx {Function File} {[@var{q}, @var{err}] =} quadgk (@dots{})
## Numerically evaluate integral using adaptive Gauss-Konrod quadrature.
## The formulation is based on a proposal by L.F. Shampine,
## @cite{"Vectorized adaptive quadrature in @sc{matlab}", Journal of
## Computational and Applied Mathematics, pp131-140, Vol 211, Issue 2,
## Feb 2008} where all function evaluations at an iteration are
## calculated with a single call to @var{f}.  Therefore the function
## @var{f} must be of the form @code{@var{f} (@var{x})} and accept
## vector values of @var{x} and return a vector of the same length
## representing the function evaluations at the given values of @var{x}.
## The function @var{f} can be defined in terms of a function handle,
## inline function or string.
##
## The bounds of the quadrature @code{[@var{a}, @var{b}]} can be finite
## or infinite and contain weak end singularities.  Variable
## transformation will be used to treat infinite intervals and weaken
## the singularities.  For example
##
## @example
## quadgk(@@(x) 1 ./ (sqrt (x) .* (x + 1)), 0, Inf)
## @end example
##
## @noindent
## Note that the formulation of the integrand uses the
## element-by-element operator @code{./} and all user functions to
## @code{quadgk} should do the same.
##
## The absolute tolerance can be passed as a fourth argument in a manner
## compatible with @code{quadv}.  Equally the user can request that
## information on the convergence can be printed is the fifth argument
## is logically true.
##
## Alternatively, certain properties of @code{quadgk} can be passed as
## pairs @code{@var{prop}, @var{val}}.  Valid properties are
##
## @table @code
## @item AbsTol
## Defines the absolute error tolerance for the quadrature.  The default
## absolute tolerance is 1e-10.
##
## @item RelTol
## Defines the relative error tolerance for the quadrature.  The default
## relative tolerance is 1e-5.
##
## @item MaxIntervalCount
## @code{quadgk} initially subdivides the interval on which to perform
## the quadrature into 10 intervals.  Sub-intervals that have an
## unacceptable error are sub-divided and re-evaluated.  If the number of
## sub-intervals exceeds at any point 650 sub-intervals then a poor
## convergence is signaled and the current estimate of the integral is
## returned.  The property 'MaxIntervalCount' can be used to alter the
## number of sub-intervals that can exist before exiting.
##
## @item WayPoints
## If there exists discontinuities in the first derivative of the
## function to integrate, then these can be flagged with the
## @code{"WayPoints"} property.  This forces the ends of a sub-interval
## to fall on the breakpoints of the function and can result in
## significantly improved estimation of the error in the integral, faster
## computation or both.  For example,
##
## @example
## quadgk (@@(x) abs (1 - x .^ 2), 0, 2, 'Waypoints', 1)
## @end example
##
## @noindent
## signals the breakpoint in the integrand at @code{@var{x} = 1}.
##
## @item Trace
## If logically true, then @code{quadgk} prints information on the
## convergence of the quadrature at each iteration.
##@end table
##
## If any of @var{a}, @var{b} or @var{waypoints} is complex, then the
## quadrature is treated as a contour integral along a piecewise
## continuous path defined by the above.  In this case the integral is
## assumed to have no edge singularities.  For example
##
## @example
## @group
## quadgk (@@(z) log (z), 1+1i, 1+1i, "WayPoints",
##         [1-1i, -1,-1i, -1+1i])
## @end group
## @end example
##
## @noindent
## integrates @code{log (z)} along the square defined by @code{[1+1i,
##  1-1i, -1-1i, -1+1i]}
##
## If two output arguments are requested, then @var{err} returns the
## approximate bounds on the error in the integral @code{abs (@var{q} -
## @var{i})}, where @var{i} is the exact value of the integral.
##
## @seealso{triplequad, dblquad, quad, quadl, quadv, trapz}
## @end deftypefn

function [q, err] = quadgk (f, a, b, varargin)
  if (nargin < 3)
    print_usage ();
  endif

  if (b < a)
    [q, err] = quadgk (f, b, a, varargin{:});
    q = -q;
  else
    abstol = 1e-10;
    reltol = 1e-5;
    waypoints = [];
    maxint = 650;
    trace = false;

    if (nargin > 3)
      if (! ischar (varargin{1}))
	if (!isempty (varargin{1}))
	  abstol = varargin{1};
	  reltol = 0;
	endif
	if (nargin > 4)
	  trace = varargin{2};
	endif
	if (nargin > 5)
	  error ("quadgk: can not pass additional arguments to user function");
        endif
      else
	idx = 1;
	while (idx < nargin - 3)
	  if (ischar (varargin{idx}))
	    str = varargin{idx++};
	    if (strcmpi (str, "reltol"))
	      reltol = varargin{idx++};
	    elseif (strcmpi (str, "abstol"))
	      abstol = varargin{idx++};
	    elseif (strcmpi (str, "waypoints"))
	      waypoints = varargin{idx++} (:);
	      if (isreal(waypoints))
		waypoints (waypoints < a | waypoints > b) = [];
	      endif
	    elseif (strcmpi (str, "maxintervalcount"))
	      maxint = varargin{idx++};
	    elseif (strcmpi (str, "trace"))
	      trace = varargin{idx++};
	    else
	      error ("quadgk: unknown property %s", str);
	    endif
	  else
	    error ("quadgk: expecting property to be a string");
	  endif
	endwhile
	if (idx != nargin - 2)
	  error ("quadgk: expecting properties in pairs");
	endif
      endif
    endif

    ## Convert function given as a string to a function handle
    if (ischar (f))
      f = @(x) feval (f, x);
    endif

    ## Use variable subsitution to weaken endpoint singularities and to
    ## perform integration with endpoints at infinity. No transform for
    ## contour integrals
    if (iscomplex (a) || iscomplex (b) || iscomplex(waypoints))
      ## contour integral, no transform
      subs = [a; waypoints; b];
      h = b - a;
      trans = @(t) t;
    elseif (isinf (a) && isinf(b))
      ## Standard Infinite to finite integral transformation.
      ##   \int_{-\infinity_^\infinity f(x) dx = \int_-1^1 f (g(t)) g'(t) dt
      ## where 
      ##   g(t)  = t / (1 - t^2)
      ##   g'(t) =  (1 + t^2) / (1 - t^2) ^ 2
      ## waypoint transform is then
      ##   t =  (2 * g(t)) ./ (1 + sqrt(1 + 4 * g(t) .^ 2))
      if (!isempty (waypoints))
	trans = @(x) (2 * x) ./ (1 + sqrt(1 + 4 * x .^ 2));
	subs = [-1; trans(waypoints); 1];
      else
	subs = linspace (-1, 1, 11)'; 
      endif
      h = 2;
      trans = @(t) t ./ (1 - t.^2);
      f = @(t) f (t ./ (1 - t .^ 2)) .* (1 + t .^ 2) ./ ((1 - t .^ 2) .^ 2);
    elseif (isinf(a))
      ## Formula defined in Shampine paper as two separate steps. One to
      ## weaken singularity at finite end, then a second to transform to
      ## a finite interval. The singularity weakening transform is
      ##   \int_{-\infinity}^b f(x) dx = 
      ##               - \int_{-\infinity}^0 f (b - t^2) 2 t dt
      ## (note minus sign) and the finite interval transform is
      ##   \int_{-\infinity}^0 f(b - t^2)  2 t dt = 
      ##                  \int_{-1}^0 f (b - g(s) ^ 2) 2 g(s) g'(s) ds
      ## where 
      ##   g(s)  = s / (1 + s)
      ##   g'(s) = 1 / (1 + s) ^ 2
      ## waypoint transform is then
      ##   t = sqrt (b - x)
      ##   s =  - t / (t + 1)
      if (!isempty (waypoints))
	tmp = sqrt (b - waypoints);
	trans = @(x)  - x ./ (x + 1);
	subs = [0; trans(tmp); 1];
      else
	subs = linspace (0, 1, 11)'; 
      endif
      h = 1;
      trans = @(t) b - (t ./ (1 + t)).^2;
      f = @(s) - 2 * s .* f (b -  (s ./ (1 + s)) .^ 2) ./ ((1 + s) .^ 3);
    elseif (isinf(b))
      ## Formula defined in Shampine paper as two separate steps. One to
      ## weaken singularity at finite end, then a second to transform to
      ## a finite interval. The singularity weakening transform is
      ##   \int_a^\infinity f(x) dx = \int_0^\infinity f (a + t^2) 2 t dt
      ## and the finite interval transform is
      ##  \int_0^\infinity f(a + t^2)  2 t dt = 
      ##           \int_0^1 f (a + g(s) ^ 2) 2 g(s) g'(s) ds
      ## where 
      ##   g(s)  = s / (1 - s)
      ##   g'(s) = 1 / (1 - s) ^ 2
      ## waypoint transform is then
      ##   t = sqrt (x - a)
      ##   s = t / (t + 1)
      if (!isempty (waypoints))
	tmp = sqrt (waypoints - a);
	trans = @(x) x ./ (x + 1);
	subs = [0; trans(tmp); 1];
      else
	subs = linspace (0, 1, 11)'; 
      endif
      h = 1;
      trans = @(t) a + (t ./ (1 - t)).^2;
      f = @(s) 2 * s .* f (a +  (s ./ (1 - s)) .^ 2) ./ ((1 - s) .^ 3);
    else
      ## Davis, Rabinowitz, "Methods of Numerical Integration" p441 2ed.
      ## Presented in section 5 of the Shampine paper as
      ##   g(t) = ((b - a) / 2) * (t / 2 * (3 - t^2)) + (b + a) / 2
      ##   g'(t) = ((b-a)/4) * (3 - 3t^2);
      ## waypoint transform can then be found by solving for t with
      ## Maxima (solve (c + 3*t -  3^3, t);). This gives 3 roots, two of
      ## which are complex for values between a and b and so can be
      ## ignored. The third is
      ##  c = (-4*x + 2*(b+a)) / (b-a);
      ##  k = ((sqrt(c^2 - 4) + c)/2)^(1/3);
      ##  t = (sqrt(3)* 1i * (1 - k^2) - (1 + k^2)) / 2 / k;
      if (! isempty (waypoints))
	trans = @__quadgk_finite_waypoint__;
	subs = [-1; trans(waypoints, a, b); 1];
      else
	subs = linspace(-1, 1, 11)'; 
      endif
      h = 2;
      trans = @(t) ((b - a) ./ 4) * t .* (3 - t.^2) + (b + a) ./ 2;
      f = @(t) f((b - a) ./ 4 .* t .* (3 - t.^2) + (b + a) ./ 2) .* ...
           3 .* (b - a) ./ 4 .* (1 - t.^2);
    endif

    ## Split interval into at least 10 sub-interval with a 15 point
    ## Gauss-Kronrod rule giving a minimum of 150 function evaluations
    while (length (subs) < 11)
      subs = [subs' ; subs(1:end-1)' + diff(subs') ./ 2, NaN](:)(1 : end - 1);
    endwhile
    subs = [subs(1:end-1), subs(2:end)];

    warn_state = warning ("query", "Octave:divide-by-zero");

    unwind_protect
      ## Singularity will cause divide by zero warnings
      warning ("off", "Octave:divide-by-zero");

      ## Initial evaluation of the integrand on the sub-intervals
      [q_subs, q_errs] = __quadgk_eval__ (f, subs);
      q0 = sum (q_subs);
      err0 = sum (q_errs);
    
      if (isa (a, "single") || isa (b, "single") || isa (waypoints, "single"))
	myeps = eps ("single");
      else
	myeps = eps;
      endif

      first = true;
      while (true)
	## Check for sub-intervals that are too small. Test must be
	## performed in untransformed sub-intervals. What is a good
	## value for this test. Shampine suggests 100*eps
	if (any (diff (trans (subs), [], 2) / (b - a) < 100 * myeps))
	  q = q0;
	  err = err0;
	  break;
	endif

	## Quit if any evaluations are not finite (Inf or NaN)
	if (any (! isfinite (q_subs)))
	  warning ("quadgk: non finite integrand encountered"); 
	  q = q0;
	  err = err0;
	  break;
	endif

	tol = max (abstol, reltol .* abs (q0));

	## If the global error estimate is meet exit
	if (err0 < tol)
	  q = q0;
	  err = err0;
	  break;
	endif

	## Accept the sub-intervals that meet the convergence criteria
	idx = find (abs (q_errs) < tol .* diff (subs, [], 2) ./ h);
	if (first)
	  q = sum (q_subs (idx));
	  err = sum (q_errs(idx));
	  first = false;
	else
	  q0 = q + sum (q_subs);
	  err0 = err + sum (q_errs);
	  q += sum (q_subs (idx));
	  err += sum (q_errs(idx));
	endif
	subs(idx,:) = [];

	## If no remaining sub-intervals exit
	if (rows (subs) == 0)
	  break;
	endif

	if (trace)
	  disp([rows(subs), err, q0]);
	endif

	## Split remaining sub-intervals in two
	mid = (subs(:,2) + subs(:,1)) ./ 2;
	subs = [subs(:,1), mid; mid, subs(:,2)];

	## If the maximum sub-interval count is met accept remaining
	## sub-interval and exit
	if (rows (subs) > maxint)
	  warning ("quadgk: maximum interval count (%d) met", maxint);
	  q += sum (q_subs);
	  err += sum (q_errs);
	  break;
	endif

	## Evaluation of the integrand on the remaining sub-intervals
	[q_subs, q_errs] = __quadgk_eval__ (f, subs);
      endwhile

      if (err > max (abstol, reltol * abs(q)))
	warning ("quadgk: Error tolerance not met. Estimated error %g", err);
      endif
    unwind_protect_cleanup
      if (strcmp (warn_state.state, "on")) 
	warning ("on", "Octave:divide-by-zero");
      endif
    end_unwind_protect
  endif
endfunction

function [q, err] = __quadgk_eval__ (f, subs)
  ## A (15,7) point pair of Gauss-Konrod quadrature rules. The abscissa
  ## and weights are copied directly from dqk15w.f from quadpack

  persistent abscissa = [-0.9914553711208126e+00, -0.9491079123427585e+00, ...
			 -0.8648644233597691e+00, -0.7415311855993944e+00, ...
			 -0.5860872354676911e+00, -0.4058451513773972e+00, ...
			 -0.2077849550078985e+00,  0.0000000000000000e+00, ...
			  0.2077849550078985e+00,  0.4058451513773972e+00, ...
			  0.5860872354676911e+00,  0.7415311855993944e+00, ...
			  0.8648644233597691e+00,  0.9491079123427585e+00, ...
			  0.9914553711208126e+00];

  persistent weights15 = ...
      diag ([0.2293532201052922e-01,  0.6309209262997855e-01, ...
	     0.1047900103222502e+00,  0.1406532597155259e+00, ...
	     0.1690047266392679e+00,  0.1903505780647854e+00, ...
	     0.2044329400752989e+00,  0.2094821410847278e+00, ...
	     0.2044329400752989e+00,  0.1903505780647854e+00, ...
	     0.1690047266392679e+00,  0.1406532597155259e+00, ...
	     0.1047900103222502e+00,  0.6309209262997855e-01, ...
	     0.2293532201052922e-01]);

  persistent weights7  = ...
      diag ([0.1294849661688697e+00,  0.2797053914892767e+00, ...
	     0.3818300505051889e+00,  0.4179591836734694e+00, ...
	     0.3818300505051889e+00,  0.2797053914892767e+00, ...
	     0.1294849661688697e+00]);

  halfwidth = diff (subs, [], 2) ./ 2;
  center = sum (subs, 2) ./ 2;;
  x = bsxfun (@plus, halfwidth * abscissa, center);
  y = reshape (f (x(:)), size(x));

  ## This is faster than using bsxfun as the * operator can use a
  ## single BLAS call, rather than rows(sub) calls to the @times
  ## function.
  q = sum (y * weights15, 2) .* halfwidth;
  err = abs (sum (y(:,2:2:end) * weights7, 2) .* halfwidth - q);
endfunction

function t = __quadgk_finite_waypoint__ (x, a, b)
  c = (-4 .* x + 2.* (b + a)) ./ (b - a);
  k = ((sqrt(c .^ 2 - 4) + c) ./ 2) .^ (1/3);
  t = real ((sqrt(3) .* 1i * (1 - k .^ 2) - (1 + k .^ 2)) ./ 2 ./ k);
endfunction

%error (quadgk (@sin))
%error (quadgk (@sin, -pi))
%error (quadgk (@sin, -pi, pi, 'DummyArg'))

%!assert (quadgk(@sin,-pi,pi), 0, 1e-6)
%!assert (quadgk(inline('sin'),-pi,pi), 0, 1e-6)
%!assert (quadgk('sin',-pi,pi), 0, 1e-6)
%!assert (quadgk(@sin,-pi,pi,'waypoints', 0, 'MaxIntervalCount', 100, 'reltol', 1e-3, 'abstol', 1e-6, 'trace', false), 0, 1e-6)
%!assert (quadgk(@sin,-pi,pi,1e-6,false), 0, 1e-6)

%!assert (quadgk(@sin,-pi,0), -2, 1e-6)
%!assert (quadgk(@sin,0,pi), 2, 1e-6)
%!assert (quadgk(@(x) 1./sqrt(x), 0, 1), 2, 1e-6)
%!assert (quadgk (@(x) abs (1 - x.^2), 0, 2, 'Waypoints', 1), 2, 1e-6)
%!assert (quadgk(@(x) 1./(sqrt(x).*(x+1)), 0, Inf), pi, 1e-6)
%!assert (quadgk (@(z) log (z), 1+1i, 1+1i, 'WayPoints', [1-1i, -1,-1i, -1+1i]), -pi * 1i, 1e-6)

%!assert (quadgk (@(x) exp(-x .^ 2), -Inf, Inf), sqrt(pi), 1e-6)