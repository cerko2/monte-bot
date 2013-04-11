function [J, grad] = cost_function(theta, X, y)
%	Computes the cost and gradient for polynomial regression
%   J = starcraftCostFunction(theta, X, y) computes the cost of using theta as the
%   parameter for polynomial regression and the gradient of the cost
%   w.r.t. to the parameters.

% Initialize some useful values
[m, n] = size(X);

% Compute the predictions and errors
hypotheses = X * theta;
sVect = (hypotheses-y).^2;

% J function (cost)
J = 1/(m*2) * sum(sVect);

% Gradient
grad = zeros(size(theta));
grad(1) = ((1/m) * sum( (hypotheses-y).*X(:,1) ) );
for j = 2:length(theta),
	grad(j) = ((1/m) * sum( (hypotheses-y).*X(:,j) ) );
end;

end
